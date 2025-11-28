package com.example.net.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.net.R
import com.example.net.TEST_ID_KEY
import com.example.net.databinding.SocialEngineeringLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocialEngineering : Fragment() {

    private var viewBinding: SocialEngineeringLayoutBinding? = null
    private val player by lazy { context?.let { ExoPlayer.Builder(it).build() } }
    private var fragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragment = QuestionsScreenFragment(openedFromCyber = true) { startIndex, endIndex ->
            player?.seekTo((startIndex * 1000).toLong())
            player?.play()
            CoroutineScope(Dispatchers.Main).launch {
                val delaySeconds = endIndex - startIndex
                delay((delaySeconds * 1000).toLong())
                player?.pause()
            }
        }
        fragment?.arguments = bundleOf(TEST_ID_KEY to 9)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Թվային Կրթություն"
        requireActivity().findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
    }


    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.playerView?.player = player

        val mediaSource = activity?.let { DefaultDataSourceFactory(it, "exoplayer-sample") }?.let {
            ProgressiveMediaSource.Factory(it).createMediaSource(
                MediaItem.fromUri(
                    Uri.parse("asset:///pushing_video.mp4")
                )
            )
        }

        if (mediaSource != null) {
            player?.setMediaSource(mediaSource)
        }
        player?.prepare()
        player?.playWhenReady = true
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.questions_screen_fragment, fragment ?: return)?.commit()

    }

    override fun onDetach() {
        super.onDetach()
        player?.stop()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return SocialEngineeringLayoutBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).apply { viewBinding = this }.root
    }
}