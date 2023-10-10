package com.itravel04.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class SliderAdapter(
    val context: Context,
    val sliderList: ArrayList<SliderData>
) : PagerAdapter() {

    override fun getCount(): Int {
        // on below line we are returning
        // the size of slider list
        return sliderList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        // inside isViewFromobject method we are
        // returning our Relative layout object.
        // inside isViewFromobject method we are
        // returning our Relative layout object.
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layoutId = when (position) {
            0 -> R.layout.slider_item
            1 -> R.layout.slider_item2
            2 -> R.layout.slider_item3
            else -> R.layout.slider_item // Añade un layout por defecto si hay más slides.
        }

        val view: View = layoutInflater.inflate(layoutId, container, false)

        if(position != 2){
            val sliderData: SliderData = sliderList[position]
            val imageView: ImageView = view.findViewById(R.id.idIVSlider)
            imageView.setImageResource(sliderData.slideImage)
        }


        if (position == 0) {
//            val sliderDescTV: TextView = view.findViewById(R.id.idTVSliderDescription)
//            val sliderData: SliderData = sliderList[position]
//            sliderDescTV.text = sliderData.slideDescription
        }




        container.addView(view)
        return view
    }



    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // this is a destroy view method
        // which is use to remove a view.
        // this is a destroy view method
        // which is use to remove a view.
        container.removeView(`object` as RelativeLayout)
    }

}
