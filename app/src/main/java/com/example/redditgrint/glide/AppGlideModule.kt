package com.example.redditgrint.glide

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.redditgrint.MainActivity
import com.example.redditgrint.R

@GlideModule
class AppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // You can change this to make Glide more verbose
        builder.setLogLevel(Log.ERROR)
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}

// Calling glideapp.with with the most specific Activity/Fragment
// context allows it to track lifecycles for your fetch
// https://stackoverflow.com/questions/31964737/glide-image-loading-with-application-context
object Glide {
    private val width = Resources.getSystem().displayMetrics.widthPixels
    private val height = Resources.getSystem().displayMetrics.heightPixels
    private var glideOptions = RequestOptions ()
        // Options like CenterCrop are possible, but I like this one best
        // Evidently you need fitCenter or dontTransform.  If you use centerCrop, your
        // list disappears.  I think that was an old bug.
        .fitCenter()
        // Rounded corners are so lovely.
        .transform(RoundedCorners (20))
        // A placeholder image for when the network is slow
        .placeholder(R.drawable.ic_cloud_download_black_24dp)
        // If we can't fetch, give the user an indication  maybe it should
        // say "network error"
        .error(ColorDrawable(Color.RED))


    private fun fromHtml(source: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(source).toString()
        }
    }

    fun fetch(
        urlPrimary: String,
        urlFallback: String,
        imageView: ImageView
    ) {
        GlideApp.with(imageView.context)
            .asBitmap()
            .load(urlPrimary)
            .apply(glideOptions)
            .override(width, height)
            .error(
                GlideApp.with(imageView.context)
                    .asBitmap()
                    .load(urlFallback)
                    .apply(glideOptions)
                    .error(R.color.colorAccent)
            )
            .into(imageView)
    }

    fun fetch(
        urlPrimary: String,
        urlFallback: Int,
        imageView: ImageView
    ) {
        GlideApp.with(imageView.context)
            .asBitmap()
            .load(urlPrimary)
            .apply(glideOptions)
            .override(width, height)
            .error(
                GlideApp.with(imageView.context)
                    .asBitmap()
                    .load(urlFallback)
                    .apply(glideOptions)
                    .error(R.color.colorAccent)
            )
            .into(imageView)
    }


    // Please ignore, this is for our testing
    private fun assetFetch(urlString: String, imageView: ImageView) {
        GlideApp.with(imageView.context)
            .load(urlString)
            .apply(glideOptions)
            .override(width, height)
            .into(imageView)
        if (urlString.endsWith("bigcat0.jpg")) {
            imageView.tag = "bigcat0.jpg"
        } else if (urlString.endsWith("bigcat1.jpg")) {
            imageView.tag = "bigcat1.jpg"
        } else if (urlString.endsWith("bigcat2.jpg")) {
            imageView.tag = "bigcat2.jpg"
        } else if (urlString.endsWith("bigdog0.jpg")) {
            imageView.tag = "bigdog0.jpg"
        }
    }


    fun glideFetch(urlString: String, thumbnailURL: String, imageView: ImageView) {
        if (MainActivity.globalDebug) {
            assetFetch(urlString, imageView)
        } else {
            GlideApp.with(imageView.context)
                .asBitmap() // Try to display animated Gifs and video still
                .load(fromHtml(urlString))
                .apply(glideOptions)
                .error(R.color.colorAccent)
                .override(width, height)
                .error(
                    GlideApp.with(imageView.context)
                        .asBitmap()
                        .load(fromHtml(thumbnailURL))
                        .apply(glideOptions)
                        .error(R.color.colorAccent)
                        .override(500, 500)
                )
                .into(imageView)
        }
    }


}
