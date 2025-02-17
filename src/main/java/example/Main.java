//package example;
//
//import example.config.AppConfig;
//import example.facade.Facade;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//public class Main {
//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        Facade facade = context.getBean(Facade.class);
//        facade.run();
//        context.close();
//    }
//}