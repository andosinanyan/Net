package com.example.net

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.jar.JarFile
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class GenerateJsonSchema


object CiCdTest {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello World from CI/CD")
        
        var filesToProcess: List<String> = emptyList()

        if (args.isNotEmpty()) {
            println("Received changed files from arguments:")
            args.forEach { println(" - $it") }
            filesToProcess = args.toList()
        } else {
            println("No arguments received. Checking git diff locally...")
            filesToProcess = getChangedFiles()
        }
        
        val allClasses = if (filesToProcess.isNotEmpty()) {
             getClassesFromChangedFiles(filesToProcess)
        } else {
             findAllAnnotatedClasses("com.example.net")
        }
        
        processAnnotatedClasses(allClasses.toList())
    }

    fun getChangedFiles(): List<String> {
        val process = ProcessBuilder(
            "git", "diff", "--name-only"
        )
            .directory(File("."))   // project root
            .redirectErrorStream(true)
            .start()

        val result = process.inputStream.bufferedReader().readText()
        process.waitFor()

        return result
            .lines()
            .filter { it.isNotBlank() }
    }

    fun findAllAnnotatedClasses(packageName: String): Set<Class<*>> {
        val classes = mutableSetOf<Class<*>>()
        val path = packageName.replace('.', '/')
        val resources = Thread.currentThread().contextClassLoader?.getResources(path)
        resources ?: return emptySet()
        
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            val file = File(resource.file)
            if (file.isDirectory) {
                findClassesInDirectory(file, packageName, classes)
            } else if (resource.protocol == "jar") {
                 val jarPath = resource.path.substring(5, resource.path.indexOf("!"))
                 findClassesInJar(jarPath, packageName, classes)
            }
        }
        return classes
    }

    private fun findClassesInDirectory(directory: File, packageName: String, classes: MutableSet<Class<*>>) {
        val files = directory.listFiles() ?: return
        for (file in files) {
            if (file.isDirectory) {
                findClassesInDirectory(file, "$packageName.${file.name}", classes)
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.substring(0, file.name.length - 6)}"
                try {
                    val clazz = Class.forName(className)
                    if (clazz.isAnnotationPresent(GenerateJsonSchema::class.java)) {
                        classes.add(clazz)
                    }
                } catch (e: ClassNotFoundException) {
                }
            }
        }
    }
    
    private fun findClassesInJar(jarPath: String, packageName: String, classes: MutableSet<Class<*>>) {
        try {
            val jarFile = JarFile(jarPath)
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val name = entry.name
                if (name.endsWith(".class")) {
                    val className = name.replace('/', '.').substring(0, name.length - 6)
                     if (className.startsWith(packageName)) {
                        try {
                            val clazz = Class.forName(className)
                            if (clazz.isAnnotationPresent(GenerateJsonSchema::class.java)) {
                                classes.add(clazz)
                            }
                        } catch (e: Throwable) {
                        }
                     }
                }
            }
        } catch(e: Exception) {
        }
    }

    fun getClassesFromChangedFiles(files: List<String>): Set<Class<*>> {
        val classes = mutableSetOf<Class<*>>()
        for (file in files) {
             if (file.endsWith(".kt")) {
                 val javaPathStart = file.indexOf("src/main/java/")
                 if (javaPathStart != -1) {
                     val relativePath = file.substring(javaPathStart + "src/main/java/".length)
                     val className = relativePath.replace('/', '.').replace(".kt", "")
                     try {
                         val clazz = Class.forName(className)
                         if (clazz.isAnnotationPresent(GenerateJsonSchema::class.java)) {
                             classes.add(clazz)
                         }
                     } catch (e: ClassNotFoundException) {
                         println("Could not load class for file: $file")
                     }
                 }
             }
        }
        return classes
    }


    fun processAnnotatedClasses(classesToProcess: List<Class<*>>) {
        println("--- Starting JSON Schema Generation ---")

        for (kClass in classesToProcess) {
            // Check if the KClass has the @GenerateJsonSchema annotation
            println("\n[INFO] Found annotated class: ${kClass.simpleName}")
            try {
                val schema = generateSchema(kClass)
                println(schema.toString())
                println("[SUCCESS] Schema generated for ${kClass.simpleName}.")
            } catch (e: Exception) {
                System.err.println("[ERROR] Failed to generate schema for ${kClass.simpleName}: ${e.message}")
            }
        }
        println("\n--- Generation Complete ---")
    }

    fun generateSchema(rootClass: Class<*>): JsonObject {
        val jsonProperties = getJsonFromClass(rootClass).first
        return makeSchemeStructured(jsonProperties)
    }

    /**
     * Core recursive method to traverse class fields and build the JSON properties object.
     */
    private fun getJsonFromClass(
        clazz: Class<*>,
        visitedClasses: MutableSet<Class<*>> = mutableSetOf()
    ): Pair<JsonObject, MutableSet<String>> {
        if (!visitedClasses.add(clazz)) return Pair(JsonObject(), mutableSetOf())

        val jsonObject = JsonObject()
        val requiredFieldsObject = mutableSetOf<String>()

        val fields = if (clazz.isAnnotationPresent(Metadata::class.java)) {
            clazz.kotlin.memberProperties.mapNotNull { it.javaField }
        } else {
            clazz.declaredFields.toList()
        }

        for (field in fields) {
            val key = getSerializedNameAnnotation(field) ?: field.name
            val fieldType = field.type
            val newJsonObject = JsonObject()

            when {
                fieldType.isEnum -> {
                    newJsonObject.addProperty("type", "string")
                    val enumValues = JsonArray().apply {
                        getEnumValues(fieldType).forEach { add(it) }
                    }
                    newJsonObject.add("enum", enumValues)
                }
                List::class.java.isAssignableFrom(fieldType) -> {
                    val listsItemType = getListElementType(field)

                    val elementTypeJson = if (isPrimitiveOrWrapper(listsItemType) || listsItemType == String::class.java) {
                        JsonObject().apply { addProperty("type", listsItemType.simpleName.lowercase()) }
                    } else {
                        createTypeJson(listsItemType, visitedClasses)
                    }
                    newJsonObject.addProperty("type", "array")
                    newJsonObject.add("items", elementTypeJson)
                }
                isPrimitiveOrWrapper(fieldType) || fieldType == String::class.java -> {
                    // Primitive/Wrapper or String
                    newJsonObject.addProperty("type", fieldType.simpleName.lowercase())
                }
                else -> {
                    // Nested Object
                    val nestedJson = getJsonFromClass(fieldType, visitedClasses)
                    newJsonObject.addProperty("type", "object")
                    newJsonObject.add("properties", nestedJson.first)
                    newJsonObject.addProperty("additionalProperties", false)
                }
            }
            newJsonObject.addProperty("description", "")
            jsonObject.add(key, newJsonObject)
        }
        visitedClasses.remove(clazz)
        return Pair(jsonObject, requiredFieldsObject)
    }

    /**
     * Tries to determine the element type of a List field using generic type information.
     */
    private fun getListElementType(field: Field): Class<*> {
        val genericType = field.genericType
        if (genericType is ParameterizedType) {
            val actualType = genericType.actualTypeArguments.firstOrNull()
            if (actualType is Class<*>) {
                return actualType
            }
        }
        // Fallback: If generic type info is not available (type erasure), assume String
        return String::class.java
    }

    private fun getEnumValues(enumClass: Class<*>): List<String> {
        return enumClass.enumConstants?.map { (it as Enum<*>).name } ?: emptyList()
    }

    private fun isPrimitiveOrWrapper(type: Class<*>?): Boolean {
        if (type == null) return false
        // Check for primitive types, Java wrapper types
        return type.isPrimitive || type == java.lang.Boolean::class.java || type == java.lang.Byte::class.java ||
                type == java.lang.Short::class.java || type == java.lang.Integer::class.java ||
                type == java.lang.Long::class.java || type == java.lang.Float::class.java ||
                type == java.lang.Double::class.java || type == java.lang.Character::class.java
    }

    private fun getSerializedNameAnnotation(field: Field): String? {
        val annotation = field.getAnnotation(SerializedName::class.java)
        return annotation?.value
    }

    private fun createTypeJson(clazz: Class<*>, visitedClasses: MutableSet<Class<*>>): JsonObject {
        val data = getJsonFromClass(clazz, visitedClasses)
        val newJson = JsonObject()
        newJson.addProperty("type", "object")
        newJson.add("properties", data.first)
        newJson.addProperty("additionalProperties", false)
        return newJson
    }

    private fun makeSchemeStructured(generatedObject: JsonObject): JsonObject {
        val newJson = JsonObject()
        newJson.addProperty("\$schema", "https://plugins.jetbrains.com/plugin/25654-jsonscheme-generator")
        newJson.addProperty("type", "object")
        newJson.add("properties", generatedObject)
        newJson.addProperty("additionalProperties", false)
        return newJson
    }
}
