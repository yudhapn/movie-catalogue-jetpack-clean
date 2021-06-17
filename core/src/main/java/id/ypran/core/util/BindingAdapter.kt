package id.ypran.core.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

@BindingAdapter("posterUrl")
fun ImageView.loadPoster(posterUrl: String?) {
    val imageUrl = "https://image.tmdb.org/t/p/w154$posterUrl"
    bindGlideSrc(imageUrl)
}

@BindingAdapter("backDropUrl")
fun ImageView.loadBackDrop(backDropUrl: String?) {
    val imageUrl = "https://image.tmdb.org/t/p/w780$backDropUrl"
    bindGlideSrc(imageUrl)
}

@BindingAdapter(
    "glideSrc",
    "glideCenterCrop",
    "glideCircularCrop",
    requireAll = false
)
fun ImageView.bindGlideSrc(
    drawableRes: String?,
    centerCrop: Boolean = false,
    circularCrop: Boolean = false
) {
    if (drawableRes == null || drawableRes.isEmpty()) {
        return
    }

    createGlideRequest(
        context,
        drawableRes,
        centerCrop,
        circularCrop
    ).into(this)
}

fun createGlideRequest(
    context: Context,
    drawableRes: String,
    centerCrop: Boolean,
    circularCrop: Boolean
): RequestBuilder<Drawable> {
//    val url = "https://image.tmdb.org/t/p/w154$drawableRes"
    val req = Glide.with(context).load(drawableRes)
    if (centerCrop) req.centerCrop()
    if (circularCrop) req.circleCrop()
    return req
}
