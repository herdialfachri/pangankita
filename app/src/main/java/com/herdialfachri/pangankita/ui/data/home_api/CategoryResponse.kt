package com.herdialfachri.pangankita.ui.data.home_api

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
	@SerializedName("code")
	val code: Int? = null,

	@SerializedName("data")
	val data: List<CategoryItem?>? = null,

	@SerializedName("message")
	val message: String? = null
)

data class CategoryItem(
	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("photo")
	val photo: String? = null
)
