# GlassFish v3 Full ClickStack

# Step by Step tutorial to use GlassFish 3 Full Profile


## Create a Glassfish3 container

```
bees app:deploy -a my-glassfish3-app -t glassfish3-full path/to/my/app.war
```


## Create a MySQL Database

```
bees db:create my-glassfish3-db
```

## Bind the MySQL Database to the Glassfish container

```
bees app:bind -a my-glassfish3-app -db my-glassfish3-db -as mydb
```

Supported JNDI names:

 * `jdbc/mydb` : unqualified relative JNDI name is **OK**
 * `java:comp/env/jdbc/mydb`: qualified private name is **OK**
 * <del><code>java:jdbc/mydb</code></del> and <del><code>java:/jdbc/mydb</code></del>: qualified relative names are **KO**
 * <del><code>java:global/env/jdbc/mydb</code></del>: qualified global name does **NOT work**

Samples:

```java
Context ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("jdbc/mydb");
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mydb");
```

## Restart Glassfish

```
bees app:restart -a my-glassfish3-app
```

# JMX-Invoker

See [RUN@cloud >> CloudBees JMX Invoker](https://developer.cloudbees.com/bin/view/RUN/CloudBees_JMX_Invoker)
