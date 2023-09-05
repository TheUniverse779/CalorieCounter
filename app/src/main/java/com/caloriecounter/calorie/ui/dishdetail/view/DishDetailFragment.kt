package com.caloriecounter.calorie.ui.dishdetail.view

import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDishDetailBinding
import com.caloriecounter.calorie.ui.dishdetail.adapter.InformationAdapter
import com.caloriecounter.calorie.ui.dishdetail.adapter.TagAdapter
import com.caloriecounter.calorie.ui.dishdetail.model.Information
import com.caloriecounter.calorie.ui.main.model.dish.Dish

class DishDetailFragment : BaseFragment<FragmentDishDetailBinding?>() {
    private var informationAdapter : InformationAdapter? = null
    private var tagAdapter : TagAdapter? = null
    private var listInfor = ArrayList<Information>()
    public var dish : Dish? = null


    override fun getLayoutRes(): Int {
        return R.layout.fragment_dish_detail
    }

    override fun initView() {}
    override fun initData() {
        listInfor.add(Information("Calories", dish!!.nutritionalContents.energy.value))
        listInfor.add(Information("Fat", dish!!.nutritionalContents.fat))
        listInfor.add(Information("Saturated Fat", dish!!.nutritionalContents.saturatedFat))
        listInfor.add(Information("Polyunsaturated Fat", dish!!.nutritionalContents.polyunsaturatedFat))
        listInfor.add(Information("Monounsaturated Fat", dish!!.nutritionalContents.monounsaturatedFat))
        listInfor.add(Information("Trans Fat", dish!!.nutritionalContents.transFat))
        listInfor.add(Information("Sodium", dish!!.nutritionalContents.sodium))
        listInfor.add(Information("Potassium", dish!!.nutritionalContents.potassium))
        listInfor.add(Information("Carbohydrates", dish!!.nutritionalContents.carbohydrates))
        listInfor.add(Information("Fiber", dish!!.nutritionalContents.fiber))
        listInfor.add(Information("Protein", dish!!.nutritionalContents.protein))
        listInfor.add(Information("Vitamin A", dish!!.nutritionalContents.vitaminA))
        listInfor.add(Information("Vitamin C", dish!!.nutritionalContents.vitaminC))
        listInfor.add(Information("Calcium", dish!!.nutritionalContents.calcium))
        listInfor.add(Information("Iron", dish!!.nutritionalContents.iron))
        informationAdapter = InformationAdapter(mActivity, listInfor)
        binding?.rvInfor?.adapter = informationAdapter

        tagAdapter = TagAdapter(mActivity, dish?.tags)
        binding?.rvTag?.adapter = tagAdapter
    }
    override fun setListener() {}
    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}