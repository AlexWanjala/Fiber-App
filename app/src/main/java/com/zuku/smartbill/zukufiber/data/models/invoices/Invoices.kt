import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2023 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Invoices (

	@SerializedName("id") val id : Int,
	@SerializedName("invoice_no") val invoice_no : String,
	@SerializedName("invoice_date") val invoice_date : String,
	@SerializedName("company_code") val company_code : String,
	@SerializedName("order_no") val order_no : String,
	@SerializedName("billing_period") val billing_period : String,
	@SerializedName("del_no") val del_no : String,
	@SerializedName("currency") val currency : String,
	@SerializedName("customer_title") val customer_title : String,
	@SerializedName("customer_name") val customer_name : String,
	@SerializedName("customer_box_no") val customer_box_no : String,
	@SerializedName("customer_address") val customer_address : String,
	@SerializedName("customer_town") val customer_town : String,
	@SerializedName("customer_country") val customer_country : String,
	@SerializedName("customer_email") val customer_email : String,
	@SerializedName("customer_pin_no") val customer_pin_no : String,
	@SerializedName("customer_vat_no") val customer_vat_no : String,
	@SerializedName("invoice_type") val invoice_type : String,
	@SerializedName("subtotal") val subtotal : String,
	@SerializedName("vat") val vat : String,
	@SerializedName("value") val value : String,
	@SerializedName("subtotal_local") val subtotal_local : String,
	@SerializedName("vat_local") val vat_local : String,
	@SerializedName("value_local") val value_local : String,
	@SerializedName("header") val header : String,
	@SerializedName("remarks") val remarks : String,
	@SerializedName("exchange_rate") val exchange_rate : String,
	@SerializedName("etr_signature") val etr_signature : String,
	@SerializedName("failed") val failed : String,
	@SerializedName("error") val error : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("old_id") val old_id : String,
	@SerializedName("excise") val excise : String,
	@SerializedName("signed") val signed : String,
	@SerializedName("sent") val sent : String,
	@SerializedName("signed_at") val signed_at : String,
	@SerializedName("sent_at") val sent_at : String,
	@SerializedName("tax_exempt") val tax_exempt : String
)