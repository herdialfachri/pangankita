package com.herdialfachri.pangankita.ui.data.ml_api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("probability")
	val probability: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null
)
