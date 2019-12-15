======================================================================================================
1.此工具类适用于Elasticsearch 6.5.1版本。

2.此工具类使用官方推荐RestHighLevel连接Elasticsearch，在Elasticsearch7.X中，transportClient被废弃。

3.在5.X 版本中，一个index下可以创建多个type；6.X 版本中，一个index下只能存在一个type； 7.X 版本中，默认
  _doc为type，官方称在8.X 版本中，将彻底废除type，为避免后续升级带来的业务、代码大幅度改动，所以此工具类中
  暂不支持自定义type的创建，默认_doc为type。

4.如需使用此工具类，请将ESBaseServiceImpl注入到相关业务层即可。

5.5.X版本后text类型字段如需排序,请确认开启【"fielddata": true】。

6.为避免Elasticsearch中混淆_id和id，所以此工具类在添加数据时，会自动剔除除主键id以外的名为"id"的属性及属性值。

7.QueryBuilder详细用法请参考 https://www.cnblogs.com/pypua/articles/9459941.html

8.UserController为示例。
========================================================================================================

