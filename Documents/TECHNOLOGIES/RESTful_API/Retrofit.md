## **3. Retrofit (от Square)**

**Пакет:** `com.squareup.retrofit2:retrofit`  
**Тип:** Синхронный + асинхронный (с RxJava или Coroutines).  
**Плюсы:**  
✔ Очень гибкий и производительный.  
✔ Поддержка RxJava, Kotlin Coroutines.  
✔ Удобные адаптеры для JSON (Gson, Moshi).  
**Минусы:**  
❌ Не интегрируется напрямую с Spring (нужны кастомные решения).

### Пример (синхронный GET):
```java
public interface UserApi {
    @GET("users/{id}")
    Call<User> getUser(@Path("id") Long id);
}

public class RetrofitExample {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);
        Call<User> call = api.getUser(1L);
        Response<User> response = call.execute(); // Синхронный вызов
        System.out.println(response.body());
    }
}
```

---
