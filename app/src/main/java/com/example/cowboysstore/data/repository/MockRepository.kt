package com.example.cowboysstore.data.repository

import com.example.cowboysstore.data.model.Product
import com.example.cowboysstore.data.model.ProductDetails
import kotlinx.coroutines.delay
import java.util.*

class MockRepository {

    private val productIds = (0..12).map {
        UUID.randomUUID().toString()
    }

  suspend fun getProducts(): Result<List<Product>> {
      randomDelay()
      return if ((0..100).random() < 50) randomResult(productList)
      else randomResult(emptyList())
  }

  suspend fun getAppVersion() : Result<String> {
      randomDelay()
      return randomResult("1.0.0 (117)")
  }

    suspend fun authorization(
        email : String,
        password : String
    ) : Result<Boolean> {
        randomDelay()
        return if (email == "qwerty@mail.ru" && password == "12345678") {
            randomResult(true)
        } else {
            randomResult(false)
        }
    }

    suspend fun getProductDetailsById(
        id : String
    ) : Result<ProductDetails> {
        randomDelay()
        return randomResult(
            ProductDetails(
                id,
                "Nike Air Jordan",
                listOf(
                    "https://myreact.ru/wp-content/uploads/2020/08/img01-114.jpg",
                    "https://myreact.ru/wp-content/uploads/2020/06/IMG_20200630_131808.jpg",
                    "https://www.tradeinn.com/f/13856/138560672/nike-air-jordan-1-ko-retro-ajko-%D0%9A%D1%80%D0%BE%D1%81%D0%BE%D0%B2%D0%BA%D0%B8.jpg"
                ),
                "9 000",
                "Джерси",
                listOf(
                    "XL",
                    "L",
                    "M",
                    "S",
                    "XS"
                ),
                "The Tampa Bay Buccaneers are headed to Super Bowl LV! As a major fan, this is no surprise but it's definitely worth celebrating, especially after the unprecedented 2020 NFL season. Add this Tom Brady Game Jersey to your collection to ensure you're Super Bowl ready. This Nike gear features bold commemorative graphics that will let the Tampa Bay Buccaneers know they have the best fans in the league.",
                listOf(
                    "Material: 100% Polyester",
                    "Foam tongue helps reduce lace pressure.",
                    "Mesh in the upper provides a breathable and plush sensation that stretches with your foot.",
                    "Midfoot webbing delivers security. The webbing tightens around your foot when you lace up, letting you choose your fit and feel.",
                    "Nike React foam is lightweight, springy and durable. More foam means better cushioning without the bulk. A Zoom Air unit in the forefoot delivers more bounce with every step. It's top-loaded to be closer to your foot for responsiveness.",
                    "The classic fit and feel of the Pegasus is back—with a wider toe box to provide extra room. Seaming on the upper provides a better shape and fit, delivering a fresh take on an icon.",
                    "Officially licensed",
                    "Imported",
                    "Brand: Nike"
                ),
                true
            )
        )
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 1) {
            Result.failure(RuntimeException())
        } else {
                Result.success(data)
        }

    private val productList =  listOf(
        Product(
            productIds[0],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[1],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[2],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[3],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[4],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[5],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[6],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[7],
            "Nike Tampa Bay Buccaneers Super Bowl LV",
            "Джерси",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[8],
            "Nike Tampa Bay Buccaneers Super Bowl LV",
            "Джерси",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[9],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),Product(
            productIds[10],
            "Nike Air Jordan",
            "Обувь",
            "5 000",
            images[(images.indices).random()]
        ),
        Product(
            productIds[11],
            "Nike Tampa Bay Buccaneers Super Bowl LV",
            "Джерси",
            "5 000",
            images[(images.indices).random()]
        )
    )
}


private val images = listOf(
    "https://myreact.ru/wp-content/uploads/2020/08/img01-114.jpg",
    "https://myreact.ru/wp-content/uploads/2020/06/IMG_20200630_131808.jpg",
    "https://www.tradeinn.com/f/13856/138560672/nike-air-jordan-1-ko-retro-ajko-%D0%9A%D1%80%D0%BE%D1%81%D0%BE%D0%B2%D0%BA%D0%B8.jpg",
    "https://cdn.sportmaster.ru/upload/resize_cache/iblock/231/800_800_1/50506710299.jpg",
)





