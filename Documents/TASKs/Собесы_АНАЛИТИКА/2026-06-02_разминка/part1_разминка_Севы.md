Из собесы Севы  X5

```java
// справа написать что выведет и пояснить почему так?
public static void main(String... args) {
    final Object obj1 = new Object();
    final Object obj2 = new Object();

    System.out.println(obj1.equals(obj2)); // 
    System.out.println(obj1 == obj2); // 

    final String str1 = new String("string");
    final String str2 = new String("string");

    System.out.println(str1.equals(str2)); // 
    System.out.println(str1 == str2); // 

    final String str3 = "string";
    final String str4 = "string";

    System.out.println(str3.equals(str4)); // 
    System.out.println(str3 == str4); // 

    final Integer i1 = new Integer(1);
    final Integer i2 = new Integer(1);

    System.out.println(i1.equals(i2)); // 
    System.out.println(i1 == i2); // 

    final Integer i3 = 128;
    final Integer i4 = 128;

    System.out.println(i3.equals(i4)); // 
    System.out.println(i3 == i4); // 

    Long l1 = 128L;
    System.out.println(l1.equals(128)); // 
}
```