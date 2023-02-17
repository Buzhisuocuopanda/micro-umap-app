package com.mkst.umap.app.mall.common.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

@Configuration
public class MybatisPlusConfig {

	@Autowired
	private DataSource dataSource;

//	@Autowired
//	private DynamicDataSource dynamicDataSource;
//
//	@Autowired
//	private DruidConfig druidConfig;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private Interceptor[] interceptors;	

	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;
	
	@Autowired
	private CommonFieldHandler commonFieldHandler;

	/**
	 *数据加密秘钥
	 **/
//    @Value("${systemplus.encrypt.password}")
//    private String password;
//
//    @Bean
//    @ConditionalOnMissingBean(Encrypt.class)
//    Encrypt encryptor() throws Exception{ ;
//        if(StringUtils.isEmpty(password)){
//            password = "micro12sdf3211034adff32ddf9993system";
//        }
//       return new AesSupport(password);
//    }
//
//	@Bean
//	public MybatisPlusInterceptor mybatisPlusInterceptor() {
//		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//		return interceptor;
//	}

	/**
	 * mybatis-plus分页插件<br>
	 * 文档：http://mp.baomidou.com<br>
	 */
//	@Bean
//	public PaginationInterceptor paginationInterceptor() {
//		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//		paginationInterceptor.setDbType(DbType.MYSQL);
//		return paginationInterceptor;
//	}
	
//	@Bean
//	@ConditionalOnProperty(name = "systemplus.encrypt.enabled",havingValue="true")
//	public DecryptReadInterceptor decryptReadInterceptor() throws Exception {
//		DecryptReadInterceptor decryptReadInterceptor = new DecryptReadInterceptor(encryptor());
//		return decryptReadInterceptor;
//	}
//	@Bean
//	@ConditionalOnProperty(name = "systemplus.encrypt.enabled",havingValue="true")
//	public SensitiveAndEncryptWriteInterceptor sensitiveAndEncryptWriteInterceptor() throws Exception {
//		SensitiveAndEncryptWriteInterceptor sensitiveAndEncryptWriteInterceptor = new SensitiveAndEncryptWriteInterceptor(encryptor());
//		return sensitiveAndEncryptWriteInterceptor;
//	}

	/**
	 * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
	 * 配置文件和mybatis-boot的配置文件同步
	 * @return
	 */
	@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(dataSource);
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		MybatisConfiguration mbConfig = new MybatisConfiguration();
		mbConfig.setCacheEnabled(true);
		mbConfig.setUseGeneratedKeys(true);
		mbConfig.setDefaultExecutorType(ExecutorType.SIMPLE);
		mbConfig.setLogImpl(Slf4jImpl.class);
		mybatisPlus.setConfiguration(mbConfig);
//		Interceptor[] interceptors = SpringUtils.getBean(Interceptor.class);	
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			mybatisPlus.setPlugins(this.interceptors);
		}
		// MP 全局配置，更多内容进入类看注释
		GlobalConfig globalConfig = new GlobalConfig();
		// 是否显示控制台Mybatis-plus banner
		globalConfig.setBanner(false);
		//配置公共字段自动填写
		globalConfig.setMetaObjectHandler(commonFieldHandler);
		DbConfig dbConfig = new DbConfig();
		// ID 策略 AUTO->`0`("数据库ID自增") INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID") UUID->`3`("全局唯一ID")
		dbConfig.setIdType(IdType.AUTO);
//		dbConfig.setTablePrefix("tb_");
		dbConfig.setInsertStrategy(FieldStrategy.NOT_NULL);
		dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);
		dbConfig.setSelectStrategy(FieldStrategy.NOT_NULL);
		globalConfig.setDbConfig(dbConfig);
		mybatisPlus.setGlobalConfig(globalConfig);
		if (this.properties.getConfigurationProperties() != null) {
			mybatisPlus.setConfigurationProperties(this.properties.getConfigurationProperties());
		}
		if (this.databaseIdProvider != null) {
			mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return mybatisPlus;
	}

	/**
	 * 性能分析拦截器，不建议生产使用
	 */
//	@Bean
//	public PerformanceInterceptor performanceInterceptor() {
//		return new PerformanceInterceptor();
//	}
}
