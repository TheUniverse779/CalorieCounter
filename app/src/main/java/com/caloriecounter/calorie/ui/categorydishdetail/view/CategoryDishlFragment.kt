package com.caloriecounter.calorie.ui.categorydishdetail.view

import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentCategoryDishBinding
import com.caloriecounter.calorie.databinding.FragmentDishDetailBinding
import com.caloriecounter.calorie.ui.dishdetail.model.Information
import com.caloriecounter.calorie.ui.main.adapter.DishAdapter
import com.caloriecounter.calorie.ui.main.model.dish.Dish
import com.caloriecounter.calorie.ui.main.model.dish.DishData
import com.caloriecounter.calorie.util.Util
import com.google.gson.Gson

class CategoryDishlFragment : BaseFragment<FragmentCategoryDishBinding?>() {

    private var categoryName = ""
    private var dishAdapter : DishAdapter? = null

    public fun setData(categoryName : String){
        this.categoryName = categoryName
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_category_dish
    }

    override fun initView() {}
    override fun initData() {
        val schedules = Gson().fromJson<DishData>(Util.loadJSONFromAsset(context, categoryName), DishData::class.java)
        dishAdapter = DishAdapter(mActivity, schedules.items)
        binding?.rvInfor?.adapter = dishAdapter
    }
    override fun setListener() {}
    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}