package com.example.cowboysstore.data.repository

import com.example.cowboysstore.data.model.Product
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

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 5) {
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





