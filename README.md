# GlassFish v3 Full ClickStack

# Step by Step tutorial to use GlassFish 3 Full Profile

## Include mysql-connector jar in your webapp's `${war_home}/META-INF/lib`

Required if you use a datasource.

Maven sample:

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.7</version>
    <executions>
        <execution>
            <id>copy</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>copy</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.24</version>
                        <type>jar</type>
                    </artifactItem>
                </artifactItems>
                <outputDirectory>${project.build.directory}/appserver-dependency</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Create a Glassfish3 container

```
bees app:deploy -a my-glassfish3-app -t glassfish3 -RPLUGIN.SRC.glassfish3=https://cyrille-leclerc.ci.cloudbees.com/job/glassfish3-full-clickstack/lastSuccessfulBuild/artifact/glassfish3-full-plugin.zip path/to/my/app.war
```

Please don't change stack name `-t glassfish3`.


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
 * <del><code>java:global/env/jdbc/mydb</code></del>: qualified global name is **KO**

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