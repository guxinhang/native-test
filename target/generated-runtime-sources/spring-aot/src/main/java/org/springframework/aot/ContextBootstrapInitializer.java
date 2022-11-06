package org.springframework.aot;

import com.gu.nativetest.NativeTestApplication;
import com.gu.nativetest.resource.testController;
import java.lang.Override;
import java.util.List;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.codec.CodecProperties;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.boot.autoconfigure.context.LifecycleProperties;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration;
import org.springframework.boot.autoconfigure.netty.NettyProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.NettyWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartProperties;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebSessionIdResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.boot.context.properties.BoundConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.validation.beanvalidation.MethodValidationExcludeFilter;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.env.Environment;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.support.HandlerFunctionAdapter;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.reactive.function.server.support.ServerResponseResultHandler;
import org.springframework.web.reactive.resource.ResourceUrlProvider;
import org.springframework.web.reactive.result.SimpleHandlerAdapter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;
import org.springframework.web.reactive.result.view.ViewResolutionResultHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import org.springframework.web.server.session.WebSessionManager;

public class ContextBootstrapInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
  @Override
  public void initialize(GenericApplicationContext context) {
    // infrastructure
    DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
    beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

    BeanDefinitionRegistrar.of("com.gu.nativetest.NativeTestApplication", NativeTestApplication.class)
        .instanceSupplier(NativeTestApplication::new).register(beanFactory);
    BeanDefinitionRegistrar.of("testController", testController.class)
        .instanceSupplier(testController::new).register(beanFactory);
    org.springframework.boot.autoconfigure.ContextBootstrapInitializer.registerAutoConfigurationPackages_BasePackages(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration", PropertyPlaceholderAutoConfiguration.class)
        .instanceSupplier(PropertyPlaceholderAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("propertySourcesPlaceholderConfigurer", PropertySourcesPlaceholderConfigurer.class).withFactoryMethod(PropertyPlaceholderAutoConfiguration.class, "propertySourcesPlaceholderConfigurer")
        .instanceSupplier(() -> PropertyPlaceholderAutoConfiguration.propertySourcesPlaceholderConfigurer()).register(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.ContextBootstrapInitializer.registerReactiveWebServerFactoryConfiguration_EmbeddedNetty(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.ContextBootstrapInitializer.registerEmbeddedNetty_reactorServerResourceFactory(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.ContextBootstrapInitializer.registerEmbeddedNetty_nettyReactiveWebServerFactory(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration", ReactiveWebServerFactoryAutoConfiguration.class)
        .instanceSupplier(ReactiveWebServerFactoryAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("reactiveWebServerFactoryCustomizer", ReactiveWebServerFactoryCustomizer.class).withFactoryMethod(ReactiveWebServerFactoryAutoConfiguration.class, "reactiveWebServerFactoryCustomizer", ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(ReactiveWebServerFactoryAutoConfiguration.class).reactiveWebServerFactoryCustomizer(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor", ConfigurationPropertiesBindingPostProcessor.class)
        .instanceSupplier(ConfigurationPropertiesBindingPostProcessor::new).customize((bd) -> bd.setRole(2)).register(beanFactory);
    org.springframework.boot.context.properties.ContextBootstrapInitializer.registerConfigurationPropertiesBinder_Factory(beanFactory);
    org.springframework.boot.context.properties.ContextBootstrapInitializer.registerConfigurationPropertiesBinder(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.BoundConfigurationProperties", BoundConfigurationProperties.class)
        .instanceSupplier(BoundConfigurationProperties::new).customize((bd) -> bd.setRole(2)).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar.methodValidationExcludeFilter", MethodValidationExcludeFilter.class)
        .instanceSupplier(() -> MethodValidationExcludeFilter.byAnnotation(ConfigurationProperties.class)).customize((bd) -> bd.setRole(2)).register(beanFactory);
    BeanDefinitionRegistrar.of("server-org.springframework.boot.autoconfigure.web.ServerProperties", ServerProperties.class)
        .instanceSupplier(ServerProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("webServerFactoryCustomizerBeanPostProcessor", WebServerFactoryCustomizerBeanPostProcessor.class)
        .instanceSupplier(WebServerFactoryCustomizerBeanPostProcessor::new).customize((bd) -> bd.setSynthetic(true)).register(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_Jackson2ObjectMapperBuilderCustomizerConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJackson2ObjectMapperBuilderCustomizerConfiguration_standardJacksonObjectMapperBuilderCustomizer(beanFactory);
    BeanDefinitionRegistrar.of("spring.jackson-org.springframework.boot.autoconfigure.jackson.JacksonProperties", JacksonProperties.class)
        .instanceSupplier(JacksonProperties::new).register(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_JacksonObjectMapperBuilderConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonObjectMapperBuilderConfiguration_jacksonObjectMapperBuilder(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_ParameterNamesModuleConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerParameterNamesModuleConfiguration_parameterNamesModule(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_JacksonObjectMapperConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonObjectMapperConfiguration_jacksonObjectMapper(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration", JacksonAutoConfiguration.class)
        .instanceSupplier(JacksonAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("jsonComponentModule", JsonComponentModule.class).withFactoryMethod(JacksonAutoConfiguration.class, "jsonComponentModule")
        .instanceSupplier(() -> beanFactory.getBean(JacksonAutoConfiguration.class).jsonComponentModule()).register(beanFactory);
    BeanDefinitionRegistrar.of("jsonMixinModule", JsonMixinModule.class).withFactoryMethod(JacksonAutoConfiguration.class, "jsonMixinModule", ApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(JacksonAutoConfiguration.class).jsonMixinModule(attributes.get(0)))).register(beanFactory);
    org.springframework.boot.autoconfigure.http.codec.ContextBootstrapInitializer.registerCodecsAutoConfiguration_DefaultCodecsConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.http.codec.ContextBootstrapInitializer.registerDefaultCodecsConfiguration_defaultCodecCustomizer(beanFactory);
    org.springframework.boot.autoconfigure.http.codec.ContextBootstrapInitializer.registerCodecsAutoConfiguration_JacksonCodecConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.http.codec.ContextBootstrapInitializer.registerJacksonCodecConfiguration_jacksonCodecCustomizer(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration", CodecsAutoConfiguration.class)
        .instanceSupplier(CodecsAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.codec-org.springframework.boot.autoconfigure.codec.CodecProperties", CodecProperties.class)
        .instanceSupplier(CodecProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartAutoConfiguration", ReactiveMultipartAutoConfiguration.class)
        .instanceSupplier(ReactiveMultipartAutoConfiguration::new).register(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.ContextBootstrapInitializer.registerReactiveMultipartAutoConfiguration_defaultPartHttpMessageReaderCustomizer(beanFactory);
    BeanDefinitionRegistrar.of("spring.webflux.multipart-org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartProperties", ReactiveMultipartProperties.class)
        .instanceSupplier(ReactiveMultipartProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebSessionIdResolverAutoConfiguration", WebSessionIdResolverAutoConfiguration.class).withConstructor(ServerProperties.class, WebFluxProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new WebSessionIdResolverAutoConfiguration(attributes.get(0), attributes.get(1)))).register(beanFactory);
    BeanDefinitionRegistrar.of("webSessionIdResolver", WebSessionIdResolver.class).withFactoryMethod(WebSessionIdResolverAutoConfiguration.class, "webSessionIdResolver")
        .instanceSupplier(() -> beanFactory.getBean(WebSessionIdResolverAutoConfiguration.class).webSessionIdResolver()).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.webflux-org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties", WebFluxProperties.class)
        .instanceSupplier(WebFluxProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration", ErrorWebFluxAutoConfiguration.class).withConstructor(ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new ErrorWebFluxAutoConfiguration(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("errorWebExceptionHandler", ErrorWebExceptionHandler.class).withFactoryMethod(ErrorWebFluxAutoConfiguration.class, "errorWebExceptionHandler", ErrorAttributes.class, WebProperties.class, ObjectProvider.class, ServerCodecConfigurer.class, ApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(ErrorWebFluxAutoConfiguration.class).errorWebExceptionHandler(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4)))).register(beanFactory);
    BeanDefinitionRegistrar.of("errorAttributes", DefaultErrorAttributes.class).withFactoryMethod(ErrorWebFluxAutoConfiguration.class, "errorAttributes")
        .instanceSupplier(() -> beanFactory.getBean(ErrorWebFluxAutoConfiguration.class).errorAttributes()).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.web-org.springframework.boot.autoconfigure.web.WebProperties", WebProperties.class)
        .instanceSupplier(WebProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration$EnableWebFluxConfiguration", WebFluxAutoConfiguration.EnableWebFluxConfiguration.class).withConstructor(WebFluxProperties.class, WebProperties.class, ServerProperties.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> {
          WebFluxAutoConfiguration.EnableWebFluxConfiguration bean = instanceContext.create(beanFactory, (attributes) -> new WebFluxAutoConfiguration.EnableWebFluxConfiguration(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3)));
          instanceContext.method("setConfigurers", List.class)
              .resolve(beanFactory, false).ifResolved((attributes) -> bean.setConfigurers(attributes.get(0)));
          return bean;
        }).register(beanFactory);
    BeanDefinitionRegistrar.of("webFluxConversionService", FormattingConversionService.class).withFactoryMethod(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class, "webFluxConversionService")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class).webFluxConversionService()).register(beanFactory);
    BeanDefinitionRegistrar.of("webFluxValidator", Validator.class).withFactoryMethod(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class, "webFluxValidator")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class).webFluxValidator()).register(beanFactory);
    BeanDefinitionRegistrar.of("localeContextResolver", LocaleContextResolver.class).withFactoryMethod(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class, "localeContextResolver")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class).localeContextResolver()).register(beanFactory);
    BeanDefinitionRegistrar.of("webSessionManager", WebSessionManager.class).withFactoryMethod(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class, "webSessionManager", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class).webSessionManager(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("webHandler", DispatcherHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "webHandler")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).webHandler()).register(beanFactory);
    BeanDefinitionRegistrar.of("responseStatusExceptionHandler", WebExceptionHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "responseStatusExceptionHandler")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).responseStatusExceptionHandler()).register(beanFactory);
    BeanDefinitionRegistrar.of("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).withFactoryMethod(WebFluxConfigurationSupport.class, "requestMappingHandlerMapping", RequestedContentTypeResolver.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).requestMappingHandlerMapping(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("webFluxContentTypeResolver", RequestedContentTypeResolver.class).withFactoryMethod(WebFluxConfigurationSupport.class, "webFluxContentTypeResolver")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).webFluxContentTypeResolver()).register(beanFactory);
    BeanDefinitionRegistrar.of("routerFunctionMapping", RouterFunctionMapping.class).withFactoryMethod(WebFluxConfigurationSupport.class, "routerFunctionMapping", ServerCodecConfigurer.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).routerFunctionMapping(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("resourceHandlerMapping", HandlerMapping.class).withFactoryMethod(WebFluxConfigurationSupport.class, "resourceHandlerMapping", ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).resourceHandlerMapping(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("resourceUrlProvider", ResourceUrlProvider.class).withFactoryMethod(WebFluxConfigurationSupport.class, "resourceUrlProvider")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).resourceUrlProvider()).register(beanFactory);
    BeanDefinitionRegistrar.of("requestMappingHandlerAdapter", RequestMappingHandlerAdapter.class).withFactoryMethod(WebFluxConfigurationSupport.class, "requestMappingHandlerAdapter", ReactiveAdapterRegistry.class, ServerCodecConfigurer.class, FormattingConversionService.class, Validator.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).requestMappingHandlerAdapter(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3)))).register(beanFactory);
    BeanDefinitionRegistrar.of("serverCodecConfigurer", ServerCodecConfigurer.class).withFactoryMethod(WebFluxConfigurationSupport.class, "serverCodecConfigurer")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).serverCodecConfigurer()).register(beanFactory);
    BeanDefinitionRegistrar.of("webFluxAdapterRegistry", ReactiveAdapterRegistry.class).withFactoryMethod(WebFluxConfigurationSupport.class, "webFluxAdapterRegistry")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).webFluxAdapterRegistry()).register(beanFactory);
    BeanDefinitionRegistrar.of("handlerFunctionAdapter", HandlerFunctionAdapter.class).withFactoryMethod(WebFluxConfigurationSupport.class, "handlerFunctionAdapter")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).handlerFunctionAdapter()).register(beanFactory);
    BeanDefinitionRegistrar.of("simpleHandlerAdapter", SimpleHandlerAdapter.class).withFactoryMethod(WebFluxConfigurationSupport.class, "simpleHandlerAdapter")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).simpleHandlerAdapter()).register(beanFactory);
    BeanDefinitionRegistrar.of("webFluxWebSocketHandlerAdapter", WebSocketHandlerAdapter.class).withFactoryMethod(WebFluxConfigurationSupport.class, "webFluxWebSocketHandlerAdapter")
        .instanceSupplier(() -> beanFactory.getBean(WebFluxConfigurationSupport.class).webFluxWebSocketHandlerAdapter()).register(beanFactory);
    BeanDefinitionRegistrar.of("responseEntityResultHandler", ResponseEntityResultHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "responseEntityResultHandler", ReactiveAdapterRegistry.class, ServerCodecConfigurer.class, RequestedContentTypeResolver.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).responseEntityResultHandler(attributes.get(0), attributes.get(1), attributes.get(2)))).register(beanFactory);
    BeanDefinitionRegistrar.of("responseBodyResultHandler", ResponseBodyResultHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "responseBodyResultHandler", ReactiveAdapterRegistry.class, ServerCodecConfigurer.class, RequestedContentTypeResolver.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).responseBodyResultHandler(attributes.get(0), attributes.get(1), attributes.get(2)))).register(beanFactory);
    BeanDefinitionRegistrar.of("viewResolutionResultHandler", ViewResolutionResultHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "viewResolutionResultHandler", ReactiveAdapterRegistry.class, RequestedContentTypeResolver.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).viewResolutionResultHandler(attributes.get(0), attributes.get(1)))).register(beanFactory);
    BeanDefinitionRegistrar.of("serverResponseResultHandler", ServerResponseResultHandler.class).withFactoryMethod(WebFluxConfigurationSupport.class, "serverResponseResultHandler", ServerCodecConfigurer.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxConfigurationSupport.class).serverResponseResultHandler(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration$WebFluxConfig", WebFluxAutoConfiguration.WebFluxConfig.class).withConstructor(WebProperties.class, WebFluxProperties.class, ListableBeanFactory.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new WebFluxAutoConfiguration.WebFluxConfig(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5), attributes.get(6)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration$WelcomePageConfiguration", WebFluxAutoConfiguration.WelcomePageConfiguration.class)
        .instanceSupplier(WebFluxAutoConfiguration.WelcomePageConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("welcomePageRouterFunctionMapping", RouterFunctionMapping.class).withFactoryMethod(WebFluxAutoConfiguration.WelcomePageConfiguration.class, "welcomePageRouterFunctionMapping", ApplicationContext.class, WebFluxProperties.class, WebProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebFluxAutoConfiguration.WelcomePageConfiguration.class).welcomePageRouterFunctionMapping(attributes.get(0), attributes.get(1), attributes.get(2)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration", WebFluxAutoConfiguration.class)
        .instanceSupplier(WebFluxAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration$AnnotationConfig", HttpHandlerAutoConfiguration.AnnotationConfig.class).withConstructor(ApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new HttpHandlerAutoConfiguration.AnnotationConfig(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("httpHandler", HttpHandler.class).withFactoryMethod(HttpHandlerAutoConfiguration.AnnotationConfig.class, "httpHandler", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(HttpHandlerAutoConfiguration.AnnotationConfig.class).httpHandler(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration", HttpHandlerAutoConfiguration.class)
        .instanceSupplier(HttpHandlerAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.aop.AopAutoConfiguration", AopAutoConfiguration.class)
        .instanceSupplier(AopAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration", ApplicationAvailabilityAutoConfiguration.class)
        .instanceSupplier(ApplicationAvailabilityAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("applicationAvailability", ApplicationAvailabilityBean.class).withFactoryMethod(ApplicationAvailabilityAutoConfiguration.class, "applicationAvailability")
        .instanceSupplier(() -> beanFactory.getBean(ApplicationAvailabilityAutoConfiguration.class).applicationAvailability()).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration", ConfigurationPropertiesAutoConfiguration.class)
        .instanceSupplier(ConfigurationPropertiesAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration", LifecycleAutoConfiguration.class)
        .instanceSupplier(LifecycleAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("lifecycleProcessor", DefaultLifecycleProcessor.class).withFactoryMethod(LifecycleAutoConfiguration.class, "defaultLifecycleProcessor", LifecycleProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(LifecycleAutoConfiguration.class).defaultLifecycleProcessor(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.lifecycle-org.springframework.boot.autoconfigure.context.LifecycleProperties", LifecycleProperties.class)
        .instanceSupplier(LifecycleProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration", ProjectInfoAutoConfiguration.class).withConstructor(ProjectInfoProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new ProjectInfoAutoConfiguration(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.info-org.springframework.boot.autoconfigure.info.ProjectInfoProperties", ProjectInfoProperties.class)
        .instanceSupplier(ProjectInfoProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration", NettyAutoConfiguration.class).withConstructor(NettyProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> new NettyAutoConfiguration(attributes.get(0)))).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.netty-org.springframework.boot.autoconfigure.netty.NettyProperties", NettyProperties.class)
        .instanceSupplier(NettyProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration", SqlInitializationAutoConfiguration.class)
        .instanceSupplier(SqlInitializationAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.sql.init-org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties", SqlInitializationProperties.class)
        .instanceSupplier(SqlInitializationProperties::new).register(beanFactory);
    org.springframework.boot.sql.init.dependency.ContextBootstrapInitializer.registerDatabaseInitializationDependencyConfigurer_DependsOnDatabaseInitializationPostProcessor(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration", TaskExecutionAutoConfiguration.class)
        .instanceSupplier(TaskExecutionAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("taskExecutorBuilder", TaskExecutorBuilder.class).withFactoryMethod(TaskExecutionAutoConfiguration.class, "taskExecutorBuilder", TaskExecutionProperties.class, ObjectProvider.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(TaskExecutionAutoConfiguration.class).taskExecutorBuilder(attributes.get(0), attributes.get(1), attributes.get(2)))).register(beanFactory);
    BeanDefinitionRegistrar.of("applicationTaskExecutor", ThreadPoolTaskExecutor.class).withFactoryMethod(TaskExecutionAutoConfiguration.class, "applicationTaskExecutor", TaskExecutorBuilder.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(TaskExecutionAutoConfiguration.class).applicationTaskExecutor(attributes.get(0)))).customize((bd) -> bd.setLazyInit(true)).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.task.execution-org.springframework.boot.autoconfigure.task.TaskExecutionProperties", TaskExecutionProperties.class)
        .instanceSupplier(TaskExecutionProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration", TaskSchedulingAutoConfiguration.class)
        .instanceSupplier(TaskSchedulingAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("scheduledBeanLazyInitializationExcludeFilter", LazyInitializationExcludeFilter.class).withFactoryMethod(TaskSchedulingAutoConfiguration.class, "scheduledBeanLazyInitializationExcludeFilter")
        .instanceSupplier(() -> TaskSchedulingAutoConfiguration.scheduledBeanLazyInitializationExcludeFilter()).register(beanFactory);
    BeanDefinitionRegistrar.of("taskSchedulerBuilder", TaskSchedulerBuilder.class).withFactoryMethod(TaskSchedulingAutoConfiguration.class, "taskSchedulerBuilder", TaskSchedulingProperties.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(TaskSchedulingAutoConfiguration.class).taskSchedulerBuilder(attributes.get(0), attributes.get(1)))).register(beanFactory);
    BeanDefinitionRegistrar.of("spring.task.scheduling-org.springframework.boot.autoconfigure.task.TaskSchedulingProperties", TaskSchedulingProperties.class)
        .instanceSupplier(TaskSchedulingProperties::new).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration$NettyWebServerFactoryCustomizerConfiguration", EmbeddedWebServerFactoryCustomizerAutoConfiguration.NettyWebServerFactoryCustomizerConfiguration.class)
        .instanceSupplier(EmbeddedWebServerFactoryCustomizerAutoConfiguration.NettyWebServerFactoryCustomizerConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("nettyWebServerFactoryCustomizer", NettyWebServerFactoryCustomizer.class).withFactoryMethod(EmbeddedWebServerFactoryCustomizerAutoConfiguration.NettyWebServerFactoryCustomizerConfiguration.class, "nettyWebServerFactoryCustomizer", Environment.class, ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(EmbeddedWebServerFactoryCustomizerAutoConfiguration.NettyWebServerFactoryCustomizerConfiguration.class).nettyWebServerFactoryCustomizer(attributes.get(0), attributes.get(1)))).register(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration", EmbeddedWebServerFactoryCustomizerAutoConfiguration.class)
        .instanceSupplier(EmbeddedWebServerFactoryCustomizerAutoConfiguration::new).register(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.function.client.ContextBootstrapInitializer.registerClientHttpConnectorConfiguration_ReactorNetty(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.function.client.ContextBootstrapInitializer.registerReactorNetty_reactorClientHttpConnector(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration", ClientHttpConnectorAutoConfiguration.class)
        .instanceSupplier(ClientHttpConnectorAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("clientConnectorCustomizer", WebClientCustomizer.class).withFactoryMethod(ClientHttpConnectorAutoConfiguration.class, "clientConnectorCustomizer", ClientHttpConnector.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(ClientHttpConnectorAutoConfiguration.class).clientConnectorCustomizer(attributes.get(0)))).customize((bd) -> bd.setLazyInit(true)).register(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.function.client.ContextBootstrapInitializer.registerWebClientAutoConfiguration_WebClientCodecsConfiguration(beanFactory);
    org.springframework.boot.autoconfigure.web.reactive.function.client.ContextBootstrapInitializer.registerWebClientCodecsConfiguration_exchangeStrategiesCustomizer(beanFactory);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration", WebClientAutoConfiguration.class)
        .instanceSupplier(WebClientAutoConfiguration::new).register(beanFactory);
    BeanDefinitionRegistrar.of("webClientBuilder", WebClient.Builder.class).withFactoryMethod(WebClientAutoConfiguration.class, "webClientBuilder", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(beanFactory, (attributes) -> beanFactory.getBean(WebClientAutoConfiguration.class).webClientBuilder(attributes.get(0)))).customize((bd) -> bd.setScope("prototype")).register(beanFactory);
  }
}
