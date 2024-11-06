package emperorfin.android.dummyjsonproducts.domain.uilayer.event.output.product

interface ProductDataTransferObjectParams : Params {

    val id: Long
    val title: String?
    val description: String?
    val brand: String?
    val price: Double?
    val thumbnail: String?
    val image: String?

}