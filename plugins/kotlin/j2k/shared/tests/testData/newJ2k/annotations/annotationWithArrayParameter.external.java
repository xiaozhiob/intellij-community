@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SomeAnnotation {
    String[] some() default {};
    int[] same() default {};
}