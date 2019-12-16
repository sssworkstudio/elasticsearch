--------------------------------------------------------------------------------------------------------
                               ****************工具类说明*************
--------------------------------------------------------------------------------------------------------

1.此工具类适用于Elasticsearch 6.5.1版本。

2.此工具类使用官方推荐RestHighLevel连接Elasticsearch，在Elasticsearch7.X中，transportClient被废弃。

3.此工具类基于springboot，如需使用其他架构，请修改RestHighLeveClient连接配置类

4.在5.X 版本中，一个index下可以创建多个type；6.X 版本中，一个index下只能存在一个type； 7.X 版本中，默认
  _doc为type，官方称在8.X 版本中，将彻底废除type，为避免后续升级带来的业务、代码大幅度改动，所以此工具类中
  暂不支持自定义type的创建，默认_doc为type。

5.5.X版本后text类型字段如需排序,请确认开启【"fielddata": true】。

6.为避免Elasticsearch中混淆_id和id，所以此工具类在添加数据时，会自动剔除除主键id以外的名为"id"的属性及属性值。

7.QueryBuilder详细用法请参考 https://www.cnblogs.com/pypua/articles/9459941.html

8.UserController为示例。


--------------------------------------------------------------------------------------------------------
                                ****************使用说明****************
--------------------------------------------------------------------------------------------------------


1.引用pom.xml中所用到的依赖

2.在配置文件中，配置ElasticSearch连接

3.将com.example.demo.base拷贝到本地工程目录下

4.在相关业务层注入ESBaseApi，调用api即可


--------------------------------------------------------------------------------------------------------
                                ****************目录结构说明****************
--------------------------------------------------------------------------------------------------------


base                                                    ----elasticsearch工具类包
     common                                             ----公共类包
            constants                                   ----常量类包
                    CodeConstants                       ----状态码常量
                    CommonConstants                     ----公共常量
            search                                      ----elasticsearch查询实体类包
                    ESPage                              ----elasticsearch分页实体类
                    Query                               ----elasticsearch查询实体类
                    QueryPage                           ----elasticsearch分页查询实体类
            config                                      ----配置类包
                    RestHighLevelClientConfig           ----elasticsearch连接配置类
     util                                               ----工具类包
            BeanUtil                                    ----bean与map转换工具类
     ESBaseApi                                          ----elasticsearch API
     ESBaseEntity                                       ----elasticsearch公共实体类
     ESBaseServiceImpl                                  ----elasticsearch API实现类

controller                                              ----示例controller包
          UserController                                ----用户Controller
pojo                                                    ----示例pojo包
     entity                                             ----示例entity包
           User                                         ----用户实体类
     vo                                                 ----示例vo包
           UserAddResVO                                 ----用户添加vo
           UserEditResVO                                ----用户编辑vo
service                                                 ----示例service包
       impl                                             ----示例impl包
            UserServiceImpl                             ----用户API实现类
       UserService                                      ----用户API
DemoApplication                                         ----springboot启动类