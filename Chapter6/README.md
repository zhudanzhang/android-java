# 数据存储

* **瞬时数据与持久化**
    - **瞬时数据**：存储在内存中，可能因程序关闭或内存回收而丢失（例如聊天内容、用户输入等）。
    - **持久化数据**：将瞬时数据保存到存储设备中，确保即使在手机或电脑关机时数据依然存在。
* **持久化技术简介**
    - **数据持久化**：提供机制让数据在瞬时状态和持久状态之间转换。
    - **Android中的持久化方式**：
        - 文件存储
        - SharedPreferences
        - 数据库存储
        - 额外：SD卡存储（较少使用）
    - 文件存储和SharedPreferences存储毕竟只适用于保存一些简单的数据和键值对

## 文件存储

- 文件存储是Android中最基本的存储方式，适合保存简单文本或二进制数据。
- 若需存储复杂文本数据，需定义格式规范以便解析。

### 将数据存储到文件

文件被存储在设备的内部存储中，路径通常为 `/data/data/<your_package_name>/files/`

- 使用 `Context` 类的 `openFileOutput()` 方法。
  - **参数**：
    - 文件名（不含路径）
    - 操作模式（`MODE_PRIVATE`、`MODE_APPEND`）
        - `MODE_PRIVATE`是默认的操作模式，表示当指定同样文件名的时候，所写入的内容将会覆盖原文件中的内容
        - `MODE_APPEND`则表示如果该文件已存在，就往文件里面追加内容，不存在就创建新文件
  - **返回值**：
    - `FileOutputStream`对象，得到了这个对象之后就可以使用Java流的方式将数据写入到文件中
  - **查看文件**
    - `adb shell` `cd /data/data/<package name>/files/`
- **示例代码**：
    ```java
    public void save() {
        String data = "Data to save";
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ```


### 读取数据

- 使用 `Context` 类的 `openFileInput()` 方法读取文件。
- **参数**
  - 读取的文件名，系统会自动到`/data/data/<package name>/files/`目录下去加载这个文件
- **返回值**
  - FileInputStream对象，得到了这个对象之后再通过Java流的方式就可以将数据读取出来了
- **示例代码**：
    ```java
    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
    ```

### 保存与还原数据示例

1. 在`onCreate()`方法中调用`load()`方法来读取文件中存储的文本内容，如果读到的内容不为`null`，就调用`EditText`的`setText()`方法将内容填充到`EditText`里，并调用setSelection()方法将输入光标移动到文本的末尾位置以便于继续输入，然后弹出一句还原成功的提示。
2. 在对字符串进行非空判断的时候使用了`TextUtils.isEmpty()`方法，这是一个非常好用的方法，它可以一次性进行两种空值的判断。当传入的字符串等于`null`或者等于空字符串的时候，这个方法都会返回true，从而使得不需要先单独判断这两种空值再使用逻辑运算符连接起来了。

**布局**：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <EditText
        android:id="@+id/edit"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Type something here"/>
</LinearLayout>
```

**MainActivity.java**：

```java
public class MainActivity extends AppCompatActivity {
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = findViewById(R.id.edit);
        String inputText = load();
        if (!TextUtils.isEmpty(inputText)) {
            edit.setText(inputText);
            edit.setSelection(inputText.length());
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);
    }

    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
```

## SharedPreferences存储

SharedPreferences 是一种以键值对方式存储数据的机制，适合轻量级数据的持久化存储。支持多种数据类型，便于快速读写。

### 数据存储

**1. 获取 SharedPreferences 对象**

要使用 SharedPreferences，首先需要获取它的对象。可以通过以下三种方法：

1. **Context 类中的 `getSharedPreferences()` 方法**
   - 参数：
     - **名称**: 用于指定SharedPreferences文件的名称（如果不存在则创建）。
        - SharedPreferences文件都是存放在`/data/data/<package name>/shared_prefs/`目录下的
     - **模式**: 用于指定操作模式，通常使用 `MODE_PRIVATE`，表示文件仅供当前应用读写。 
        - 表示只有当前的应用程序才可以对这个SharedPreferences文件进行读写
   - 示例:
     ```java
     SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
     ```
2. **Activity 类中的 `getPreferences()` 方法**
   - 接收一个操作模式参数，因为使用这个方法时会自动将当前活动的类名作为SharedPreferences的文件名
3. **PreferenceManager 类中的 `getDefaultSharedPreferences()` 方法**
   - 静态方法，接收一个Context参数，并自动使用当前应用程序的包名作为前缀来命名SharedPreferences文件

**2. 存储数据步骤**

得到了SharedPreferences对象之后，就可以开始向Shared-Preferences文件中存储数据

存储数据的主要步骤：
1. 调用SharedPreferences对象的 `edit()` 方法获取 `SharedPreferences.Editor` 对象
2. 向SharedPreferences.Editor对象中添加数据，使用 `putXxx()` 方法添加数据（如 `putString()`、`putInt()`、`putBoolean()`）
3. 调用apply()方法将添加的数据提交，从而完成数据存储操作

**3. 示例代码**

1. 首先给按钮注册了一个点击事件，然后在点击事件中通过getShared-Preferences()方法指定SharedPreferences的文件名为data，并得到了SharedPreferences.Editor对象。
2. 接着向这个对象中添加了3条不同类型的数据，最后调用apply()方法进行提交，从而完成了数据存储的操作
3. 查看数据：`adb shell` `cd /data/data/sharedpreferencestest/shared_prefs/` data.xml 文件

```xml
<!-- activity_main.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >
    <Button
        android:id="@+id/save_data"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Save data"
        />
</LinearLayout>
```

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveData = (Button) findViewById(R.id.save_data);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data",
                    MODE_PRIVATE).edit();
                editor.putString("name", "Tom");
                editor.putInt("age", 28);
                editor.putBoolean("married", false);
                editor.apply();
            }
        });
    }
}
```

### 数据读取

**1. 从 SharedPreferences 读取数据**

`SharedPreferences`对象中提供了一系列的get方法，用于对存储的数据进行读取

- 每种`get`方法都对应了`Shared-Preferences.Editor`中的一种`put`方法
    - **getBoolean()**: 读取布尔型数据
    - **getString()**: 读取字符串
    - **getInt()**: 读取整型数据
- 每个方法接收两个参数：
    - 键：传入存储数据时使用的键就可以得到相应的值
    - 默认值：即表示当传入的键找不到对应的值时会以什么样的默认值进行返回


```xml
<!-- activity_main.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >
    <Button
        android:id="@+id/save_data"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Save data"
        />
    <Button
        android:id="@+id/restore data"
        android:layout width="match parent"
        android:layout height="wrap content"
        android:text="Restore data"
        />
</LinearLayout>
```

1. 在还原数据按钮的点击事件中首先通过`getSharedPreferences()`方法得到了`SharedPreferences`对象
2. 然后分别调用它的`getString()`、`getInt()`和`getBoolean()`方法，去获取前面所存储的姓名、年龄和是否已婚，如果没有找到相应的值，就会使用方法中传入的默认值来代替，最后通过`Log`将这些值打印出来

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ...
        Button restoreData = (Button) findViewById(R.id.restore data);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("data", MODE PRIVATE);
                String name = pref.getString("name", "");
                int age = pref.getInt("age", 0);
                boolean married = pref.getBoolean("married", false);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "age is " + age);
                Log.d("MainActivity", "married is " + married);
            }
        });
    }
}
```


### 记住密码示例

1. 首先在`onCreate()`方法中获取到了`SharedPreferences`对象，然后调用它的`getBoolean()`方法去获取`remember_password`这个键对应的值
    * 一开始当然不存在对应的值了，所以会使用默认值`false`，这样就什么都不会发生
2. 在登录成功之后，会调用`CheckBox`的`isChecked()`方法来检查复选框是否被选中，如果被选中了，则表示用户想要记住密码，这时将`remember_password`设置为`true`，然后把`account`和`password`对应的值都存入到`SharedPreferences`文件当中并提交
3. 如果没有被选中，就简单地调用一下`clear()`方法，将`SharedPreferences`文件中的数据全部清除掉。
4. 当用户选中了记住密码复选框，并成功登录一次之后，`remember_password`键对应的值就是`true`了，这个时候如果再重新启动登录界面，就会从`SharedPreferences`文件中将保存的账号和密码都读取出来，并填充到文本输入框中，然后把记住密码复选框选中，这样就完成记住密码的功能了


**1. 布局修改**

```xml
<!-- activity_login.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    ...
    <LinearLayout
        android:orientation="horizontal"
        android:layout width="match parent"
        android:layout height="wrap content">
        <CheckBox
            android:id="@+id/remember pass"
            android:layout width="wrap content"
            android:layout height="wrap content" />
        <TextView
            android:layout width="wrap content"
            android:layout height="wrap content"
            android:textSize="18sp"
            android:text="Remember password" />
    </LinearLayout>
    <Button
        android:id="@+id/login"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:text="Login" />
</LinearLayout>
```

**LoginActivity 示例代码**

```java
public class LoginActivity extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox rememberPass;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_pass);
        
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            accountEdit.setText(pref.getString("account", ""));
            passwordEdit.setText(pref.getString("password", ""));
            rememberPass.setChecked(true);
        }

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (account.equals("admin") && password.equals("123456")) {
                    editor = pref.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    // 转到主界面
                } else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
```

## SQLite数据库存储

- **SQLite简介**：轻量级关系型数据库，运算速度快，占用资源少（通常只需几百KB内存），适合移动设备
- **特点**：
  - 支持标准SQL语法
  - 遵循ACID事务
  - 简单易用，无需设置用户名和密码
- **适合存储复杂的关系型数据**，超越文件存储和SharedPreferences

### 创建数据库

- **SQLiteOpenHelper类**：Android提供的帮助类，简化数据库创建和升级过程
  - **抽象类**：需要创建子类继承
  - **抽象类方法**：
    - `onCreate(SQLiteDatabase db)`: 创建数据库时调用，处理一些创建表的逻辑
    - `onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)`: 数据库升级时调用
  - **实例方法**：
    - `getReadableDatabase()`: 创建或打开一个现有的数据库
        * 如果数据库已存在则直接打开，否则创建一个新的数据库​，并返回一个可对数据库进行读写操作的对象
        * 当数据库不可写入的时候（如磁盘空间已满）返回的对象将以只读的方式去打开数据库
    - `getWritableDatabase()`: 创建或打开一个现有的数据库
        * 如果数据库已存在则直接打开，否则创建一个新的数据库​，并返回一个可对数据库进行读写操作的对象
        * 当数据库不可写入的时候（如磁盘空间已满）将出现异常
  - **数据库文件**
    - 存放在`/data/data/<package name>/databases/`目录下
* **构造方法**
    - `MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)`：
        - `context`: 操作数据库所需的上下文
        - `name`: 数据库名称
        - `factory`: 自定义Cursor，通常传入null
        - `version`: 数据库版本号

**创建数据库及表**

1. **建表语句**：
   ```sql
   create table Book (
       id integer primary key autoincrement,
       author text,
       price real,
       pages integer,
       name text
   )
   ```
   - 数据类型：
     - `integer`: 整型
     - `real`: 浮点型
     - `text`: 文本类型
     - `blob`: 二进制类型
   - 主键：`primary key`，自增长：`autoincrement`
2. **自定义数据库帮助类继承自SQLiteOpenHelper** ：
   ```java
   public class MyDatabaseHelper extends SQLiteOpenHelper {
       public static final String CREATE_BOOK = "create table Book (" +
               "id integer primary key autoincrement, " +
               "author text, " +
               "price real, " +
               "pages integer, " +
               "name text)";
       private Context mContext;
       
       public MyDatabaseHelper(Context context, String name,
                               SQLiteDatabase.CursorFactory factory, int version) {
           super(context, name, factory, version);
           mContext = context;
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL(CREATE_BOOK);
           Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       }
   }
   ```
3. **布局文件（activity_main.xml）**：
   ```xml
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <Button
           android:id="@+id/create_database"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Create database" />
   </LinearLayout>
   ```
4. **代码实现**：
   1. 在`onCreate()`方法中构建了一个`MyDatabaseHelper`对象，并且通过构造函数的参数将数据库名指定为`BookStore.db`，版本号指定为`1`，然后在`Create database`按钮的点击事件里调用了`getWritableDatabase()`方法。
   2. 这样当第一次点击`Create database`按钮时，就会检测到当前程序中并没有`BookStore.db`这个数据库，于是会创建该数据库并调用`MyDatabaseHelper`中的`onCreate()`方法，这样Book表也就得到了创建，然后会弹出一个Toast提示创建成功。
   3. 再次点击`Create database`按钮时，会发现此时已经存在`BookStore.db`数据库了，因此不会再创建一次
   ```java
   public class MainActivity extends AppCompatActivity {
       private MyDatabaseHelper dbHelper;

       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
           Button createDatabase = findViewById(R.id.create_database);
           createDatabase.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dbHelper.getWritableDatabase();
               }
           });
       }
   }
   ```
5. 验证数据库和表的创建
    * `adb shell`
    * `cd /data/data/com.example.databasetest/databases/`
    * `ls`
    * `# sqlite3 BookStore.db`
        * 借助sqlite命令来打开数据库了，只需要键入sqlite3，后面加上数据库名即可
        * 键入.table命令查看表
        * 通过.schema命令来查看的建表语句
        * 键入.exit或.quit命令可以退出数据库的编辑

### 升级数据库

- **onUpgrade()方法**：用于数据库升级的重要方法，确保在数据结构变更时有效管理数据库。
- **缺点**：会造成数据丢失，每当升级一次数据库，之前表中的数据就全没了

**添加新表示例**

- **新表创建需求**：添加`Category`表以记录图书分类。
- **建表语句**：
   ```sql
   create table Category (
       id integer primary key autoincrement,
       category_name text,
       category_code integer
   )
   ```

**修改MyDatabaseHelper**

1. **新增常量**：
   ```java
   public static final String CREATE_CATEGORY = "create table Category (" +
           "id integer primary key autoincrement, " +
           "category_name text, " +
           "category_code integer)";
   ```
   
2. **onCreate()方法更新**：
   ```java
   @Override
   public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_BOOK);
       db.execSQL(CREATE_CATEGORY);
       Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
   }
   ```

**运行程序**

- **创建表失败原因**：由于`BookStore.db`已存在，`onCreate()`方法不会再次执行，因此新表无法创建。
- **解决方法**：卸载应用后重新运行，但这不是推荐的做法。

**使用onUpgrade()方法**



```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists Book");
    db.execSQL("drop table if exists Category");
    onCreate(db);
}
```
1. 使用`DROP`语句删除已有表
2. 然后再调用`onCreate()`方法重新创建。这里先将已经存在的表删除掉，因为如果在创建表时发现这张表已经存在了，就会直接报错

**修改数据库版本**

- **版本号更新**：在`MainActivity`中，将数据库版本号改为`2`
    ```java
    dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
    ```
- 提高版本号以触发`onUpgrade()`方法

**验证创建成功**

1. **运行程序并点击按钮**：在创建数据库时，会弹出创建成功的提示
2. **使用adb shell检查**：
   - 输入`.tables`命令查看表
   - 输入`.schema`命令查看建表语句

### 添加数据

**1. CRUD 操作概述**

- CRUD 代表四种基本数据操作：创建（Create）、查询（Retrieve）、更新（Update）、删除（Delete）。
- 每种操作对应的SQL命令：
  - 添加数据：`INSERT`
  - 查询数据：`SELECT`
  - 更新数据：`UPDATE`
  - 删除数据：`DELETE`

**2. SQLiteDatabase 对象**

- 通过调用 `SQLiteOpenHelper` 的 `getReadableDatabase()` 或 `getWritableDatabase()` 方法获取 `SQLiteDatabase` 对象
- 该对象可用于执行 CRUD 操作

**3. 添加数据（Create）**

- 使用 `insert()` 方法向数据库表中添加数据。
  - 方法接收三个参数：
    - 第一个参数：表名（如 `"Book"`）。
    - 第二个参数：用于在未指定添加数据的情况下自动赋值的列（一般传入 `null`）。
    - 第三个参数：`ContentValues` 对象，包含要插入的数据。
  
**4. ContentValues**

- `ContentValues` 用于存储要插入的列名和值。
- 使用 `put()` 方法添加列名和对应值。

**5. 示例代码**

1. 先获取到了SQLiteDatabase对象，然后使用ContentValues来对要添加的数据进行组装。
2. 接下来调用了insert()方法将数据添加到表当中，注意这里实际上添加了两条数据，上述代码中使用ContentValues分别组装了两次不同的内容，并调用了两次insert()方法。

- 修改 `activity_main.xml` 布局文件，添加一个“Add data”按钮：
  ```xml
  <Button
      android:id="@+id/add_data"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:text="Add data" />
  ```
- 在 `MainActivity` 中实现添加数据的逻辑：
  ```java
  Button addData = findViewById(R.id.add_data);
  addData.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          SQLiteDatabase db = dbHelper.getWritableDatabase();
          ContentValues values = new ContentValues();
          // 添加第一条数据
          values.put("name", "The Da Vinci Code");
          values.put("author", "Dan Brown");
          values.put("pages", 454);
          values.put("price", 16.96);
          db.insert("Book", null, values); // 插入第一条数据

          // 添加第二条数据
          values.clear();
          values.put("name", "The Lost Symbol");
          values.put("author", "Dan Brown");
          values.put("pages", 510);
          values.put("price", 19.95);
          db.insert("Book", null, values); // 插入第二条数据
      }
  });
  ```

**6. 数据验证** 

- 运行程序并点击“Add data”按钮。
- 使用 SQL 查询语句 `sqlite> SELECT * FROM Book;` 验证数据是否成功添加。

### 更新数据

**1. 更新数据概述**

- 使用 `update()` 方法更新表中已有的数据。
- 方法接收四个参数：
  1. **表名**：指定要更新的表。
  2. **ContentValues**：ContentValues对象，要把更新数据在这里组装进去
  3. **whereClause**（可选）：约束更新的行，类似于SQL中的WHERE条件。
  4. **whereArgs**（可选）：用于替换`whereClause`中的占位符。

**2. 修改布局文件**

- 在 `activity_main.xml` 中添加一个更新数据的按钮：
  ```xml
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
        ...
        <Button
            android:id="@+id/update data"
            android:layout width="match parent"
            android:layout height="wrap content"
            android:text="Update data"
            />
    </LinearLayout>
  ```

**3. 修改 MainActivity**

- 在按钮的点击事件中：
  - 创建 `ContentValues` 对象并设置新价格。
  - 调用 `update()` 方法进行更新：
    - 第一个参数为表名 `"Book"`。
    - 第二个参数为 `values`，包含更新的数据。
    - 第三个参数为 `"name = ?"`，用于指定更新的条件。
    - 第四个参数为 `new String[] { "The Da Vinci Code" }`，为条件中的占位符提供具体值

- 在 `MainActivity` 中实现更新数据的逻辑：
  ```java
  public class MainActivity extends AppCompatActivity {
      private MyDatabaseHelper dbHelper;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

          Button updateData = findViewById(R.id.update_data);
          updateData.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SQLiteDatabase db = dbHelper.getWritableDatabase();
                  ContentValues values = new ContentValues();
                  values.put("price", 10.99); // 更新价格
                  db.update("Book", values, "name = ?", new String[] { "The Da Vinci Code" });
              }
          });
      }
  }
  ```


**4. 数据验证**
- 点击“Update data”按钮后，使用 SQL 查询语句查看表中数据，以验证价格是否已成功更新。

### 删除数据


**1. 删除数据概述**

- 使用 `delete()` 方法从表中删除数据。
- 方法接收三个参数：
  1. **表名**：指定要删除数据的表。
  2. **whereClause**（可选）：约束删除的行，类似于SQL中的WHERE条件。
  3. **whereArgs**（可选）：用于替换`whereClause`中的占位符。

**2. 修改布局文件**

- 在 `activity_main.xml` 中添加一个删除数据的按钮：
  ```xml
  <Button
      android:id="@+id/delete_data"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:text="Delete data" />
  ```

**3. 修改 MainActivity**

- 在按钮的点击事件中：
  - 使用 `delete()` 方法进行删除操作：
    - 第一个参数为表名 `"Book"`。
    - 第二个参数为 `"pages > ?"`，用于指定删除条件。
    - 第三个参数为 `new String[] { "500" }`，为条件中的占位符提供具体值，表示删除页数超过500的书籍。

- 在 `MainActivity` 中实现删除数据的逻辑：
  ```java
  public class MainActivity extends AppCompatActivity {
      private MyDatabaseHelper dbHelper;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

          Button deleteButton = findViewById(R.id.delete_data);
          deleteButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SQLiteDatabase db = dbHelper.getWritableDatabase();
                  db.delete("Book", "pages > ?", new String[] { "500" });
              }
          });
      }
  }
  ```

**4. 数据验证**

- 点击“Delete data”按钮后，使用 SQL 查询语句查看表中数据，以验证符合条件的书籍是否已成功删除。

### 查询数据

- 查询是CRUD操作中最复杂的一种，使用SQL语言进行数据检索。
- 在Android中，使用 `SQLiteDatabase` 的 `query()` 方法进行查询。

**1. query() 方法参数**

`query()` 方法的基本重载需要传入7个参数：

1. **table**：要查询的表名。
2. **columns**：指定查询的列，如果为null则查询所有列。
3. **selection**：用于约束查询的条件，类似于SQL中的WHERE。
4. **selectionArgs**：用于替换selection中的占位符。
5. **groupBy**：指定需要进行分组的列。
6. **having**：对分组后的数据进行进一步的过滤。
7. **orderBy**：指定查询结果的排序方式。

调用`query()`方法后会返回一个`Cursor`对象，查询到的所有数据都将从这个对象中取出

**2. 简单查询示例**

- 修改 `activity_main.xml` 文件，添加一个查询数据的按钮：
  ```xml
  <Button
      android:id="@+id/query_data"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:text="Query data" />
  ```

**3. 修改 MainActivity**

- 在按钮的点击事件中：
  - 调用 `query()` 方法，查询 `Book` 表中所有数据。
  - 参数中第一个为表名，其余参数为null，表示查询所有行和列。
  - 使用 `Cursor` 对象遍历查询结果，通过 `getColumnIndex()` 方法获取列索引并提取数据。
- 调用`close()`方法来关闭`Cursor`

- 在 `MainActivity` 中实现查询数据的逻辑：
  ```java
  public class MainActivity extends AppCompatActivity {
      private MyDatabaseHelper dbHelper;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

          Button queryButton = findViewById(R.id.query_data);
          queryButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SQLiteDatabase db = dbHelper.getWritableDatabase();
                  Cursor cursor = db.query("Book", null, null, null, null, null, null);
                  if (cursor.moveToFirst()) {
                      do {
                          String name = cursor.getString(cursor.getColumnIndex("name"));
                          String author = cursor.getString(cursor.getColumnIndex("author"));
                          int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                          double price = cursor.getDouble(cursor.getColumnIndex("price"));
                          Log.d("MainActivity", "book name is " + name);
                          Log.d("MainActivity", "book author is " + author);
                          Log.d("MainActivity", "book pages is " + pages);
                          Log.d("MainActivity", "book price is " + price);
                      } while (cursor.moveToNext());
                  }
                  cursor.close();
              }
          });
      }
  }
  ```

**4. 数据验证**

- 点击“Query data”按钮后，查看 `logcat` 的打印内容，以验证数据是否正确读取。

### 使用SQL操作数据库

Android提供了API，允许开发者直接使用SQL语句来操作数据库，适合习惯使用SQL的开发者。

**1. 添加数据**

- 使用 `execSQL()` 方法添加数据：
  ```java
  db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)",
             new String[] { "The Da Vinci Code", "Dan Brown", "454", "16.96" });
  db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)",
             new String[] { "The Lost Symbol", "Dan Brown", "510", "19.95" });
  ```

**2. 更新数据**

- 使用 `execSQL()` 方法更新数据：
  ```java
  db.execSQL("update Book set price = ? where name = ?", new String[] { "10.99", "The Da Vinci Code" });
  ```

**3. 删除数据**

- 使用 `execSQL()` 方法删除数据：
  ```java
  db.execSQL("delete from Book where pages > ?", new String[] { "500" });
  ```

**4. 查询数据**

- 使用 `rawQuery()` 方法查询数据：
  ```java
  Cursor cursor = db.rawQuery("select * from Book", null);
  ```

**5. 方法总结**
- 所有操作除了查询使用 `rawQuery()`，其他均使用 `execSQL()` 方法。
- 以上操作结果与之前通过API进行CRUD的结果相同。

## 使用LitePal操作数据库

**LitePal简介**

- LitePal是一个开源的Android数据库框架，使用对象关系映射（ORM）模式。
- 该框架封装了常用的数据库功能，允许开发者在不编写SQL语句的情况下进行数据库操作（增删改查）。
- 适用于提高开发效率和代码稳定性。

###  配置 LitePal

> [LitePal](https://github.com/LitePalFramework/LitePal)

1. **创建项目**：新建一个LitePalTest项目。
2. **添加依赖**：
   - 打开 `app/build.gradle` 文件，在 `dependencies` 闭包中添加以下内容：
     ```groovy
     dependencies {
         compile fileTree(dir: 'libs', include: ['*.jar'])
         compile 'com.android.support:appcompat-v7:23.2.0'
         testCompile 'junit:junit:4.12'
         compile 'org.litepal.android:core:1.4.1'
     }
     ```
   - 这里的 `1.4.1` 是LitePal的版本号，可以在项目主页查看最新版本。
3. **创建配置文件**：
   - 在 `app/src/main` 目录下新建一个 `assets` 目录。
   - 在 `assets` 目录中创建 `litepal.xml` 文件，并编辑内容如下：
     ```xml
     <? xml version="1.0" encoding="utf-8"? >
     <litepal>
         <dbname value="BookStore" ></dbname>
         <version value="1" ></version>
         <list>
         </list>
     </litepal>
     ```
   - `<dbname>` 标签指定数据库名，`<version>` 标签指定数据库版本号,`<list>`标签用于指定所有的映射模型
4. **配置应用程序**：
   - 修改 `AndroidManifest.xml` 文件中的 `application` 标签：
     ```xml
     <manifest xmlns:android="http://schemas.android.com/apk/res/android"
         package="com.example.litepaltest">
         <application
             android:name="org.litepal.LitePalApplication"
             android:allowBackup="true"
             android:icon="@mipmap/ic_launcher"
             android:label="@string/app_name"
             android:supportsRtl="true"
             android:theme="@style/AppTheme">
             ...
         </application>
     </manifest>
     ```
   - 通过设置 `android:name` 为 `org.litepal.LitePalApplication` 来确保LitePal的所有功能正常工作。

### 创建和升级数据库

1. **对象关系映射（ORM）概述**：
   - LitePal使用**对象关系映射模式**，简化数据库操作。
   - 通过面向对象的方式来操作数据库，减少直接使用SQL的需求。
2. **创建Book类**：
   - 定义一个`Book`类作为数据库表的映射：
    ```java
     public class Book {
        private int id;
        private String author;
        private double price;
        private int pages;
        private String name;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getAuthor() {
            return author;
        }
        public void setAuthor(String author) {
            this.author = author;
        }
        public double getPrice() {
            return price;
        }
        public void setPrice(double price) {
            this.price = price;
        }
        public int getPages() {
            return pages;
        }
        public void setPages(int pages) {
            this.pages = pages;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
    ```
   - 类中的字段对应数据库表中的列
3. **配置映射模型**：
   - 在`litepal.xml`中添加Book类的映射：
     ```xml
     <litepal>
         <dbname value="BookStore"></dbname>
         <version value="1"></version>
         <list>
             <mapping class="com.example.litepaltest.Book"></mapping>
         </list>
     </litepal>
     ```
    - 使用`<mapping>`标签来声明要配置的映射模型类
4. **创建数据库**：
   - 在`MainActivity`中添加数据库创建逻辑：
     ```java
     public class MainActivity extends AppCompatActivity {
         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
             Button createDatabase = (Button) findViewById(R.id.create_database);
             createDatabase.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     LitePal.getDatabase();
                 }
             });
         }
     }
     ```
   - 点击按钮触发`LitePal.getDatabase()`方法，**自动创建数据库**
5. **查看数据库**：
   - 通过`adb shell`查看数据库创建情况。
        - `cd /data/data/com.example.litepaltest`
        - `cd databases`
   - 使用`sqlite3`命令查看`BookStore.db`文件中的表结构
        - 输入`.schema`命令查看建表语句
6. **数据库升级**：
   - 修改`Book`类，添加`press`字段：
     ```java
     public class Book {
        ...
        private String press;
        ...
        public String getPress() {
            return press;
        }
        public void setPress(String press) {
            this.press = press;
        }
     }
     ```
   - 新建`Category`类：
     ```java
     public class Category {
         private int id;
         private String categoryName;
         private int categoryCode;
         // Setter方法...
     }
     ```
   - 更新`litepal.xml`，增加映射模型和版本号：
     ```xml
     <litepal>
         <dbname value="BookStore"></dbname>
         <version value="2"></version>
         <list>
             <mapping class="com.example.litepaltest.Book"></mapping>
             <mapping class="com.example.litepaltest.Category"></mapping>
         </list>
     </litepal>
     ```
7. **重新运行程序**：
   - 点击Create database按钮，查看最新的建表语句。
   - 使用`sqlite3`命令查看`BookStore.db`文件中的表结构
        - 输入`.schema`命令查看建表语句
   - 确认`book`表中新增的`press`列及`category`表的创建。
   - LitePal自动保留了之前表中的数据，避免数据丢失。

### 使用LitePal添加数据

1. **回顾添加数据的方法**：
   - 传统方法需要使用`ContentValues`对象，并通过`SQLiteDatabase`的`insert()`方法将数据添加到数据库表中。

2. **LitePal添加数据的简化过程**：
   - 使用LitePal，数据添加变得简单。
   - 只需创建模型类实例、设置数据，然后调用`save()`方法即可。

3. **模型类继承**：
   - 在进行CRUD操作时，模型类必须继承自`DataSupport`类。
   - 修改`Book`类的定义：
     ```java
     public class Book extends DataSupport {
         ...
     }
     ```

4. **向`Book`表添加数据**：
   1. 首先是创建出了一个Book的实例，然后调用Book类中的各种`set`方法对数据进行设置
   2. 最后再调用`book.save()`方法就能完成数据添加操作
        * `save()`方法来自于`DataSupport`类
     ```java
     public class MainActivity extends AppCompatActivity {
         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
             Button addData = (Button) findViewById(R.id.add_data);
             addData.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Book book = new Book();
                     book.setName("The Da Vinci Code");
                     book.setAuthor("Dan Brown");
                     book.setPages(454);
                     book.setPrice(16.96);
                     book.setPress("Unknown");
                     book.save();
                 }
             });
         }
     }

5. **验证数据添加**：
   - 重新运行程序，点击“Add data”按钮。
   - 打开`BookStore.db`数据库，执行SQL查询语句：
     `SELECT * FROM Book;`
   - 验证作者、书名、页数、价格、出版社等数据是否准确添加。

### 使用LitePal更新数据

1. **方式一：基本更新方式**：
   - 对**已存储的对象重新设值**后调用`save()`方法即可更新数据。
   - 使用`model.isSaved()`判断对象是否已存储，返回`true`表示已存储，`false`表示未存储。

2. **已存储对象的判断**：
   - `isSaved()`返回`true`的情况（model会被认为是已存储的对象）：
     1. 已经调用过`save()`方法添加数据
     2. 对象通过LitePal查询API获取，由于是从数据库中查到的对象，因此也会被认为是已存储的对象

3. **修改`MainActivity`以更新数据**：
   - 示例代码：
     ```java
     public class MainActivity extends AppCompatActivity {
         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
             Button updateData = (Button) findViewById(R.id.update_data);
             updateData.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Book book = new Book();
                     book.setName("The Lost Symbol");
                     book.setAuthor("Dan Brown");
                     book.setPages(510);
                     book.setPrice(19.95);
                     book.setPress("Unknown");
                     book.save();
                     book.setPrice(10.99);
                     book.save();
                 }
             });
         }
     }
     ```

4. **验证更新操作**：
   - 运行程序并点击“Update data”按钮。
   - 查询Book表，验证价格更新成功。

5. **方式二：更灵活的更新方式**：
   - 使用`updateAll()`方法进行条件更新
   - 指定将所有书名是The Lost Symbol并且作者是Dan Brown的书价格更新为14.95，出版社更新为Anchor
   - 示例代码：
     ```java
     public class MainActivity extends AppCompatActivity {
         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_main);
             Button updateData = (Button) findViewById(R.id.update_data);
             updateData.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Book book = new Book();
                     book.setPrice(14.95);
                     book.setPress("Anchor");
                     book.updateAll("name = ? and author = ?", "The Lost Symbol", "Dan Brown");
                 }
             });
         }
     }
     ```

6. **`updateAll()`方法**：
   - 指定一个条件约束，指定条件更新特定记录
   - 如果不指定条件，默认更新所有记录

7. **更新字段为默认值**：
   - 使用`setToDefault()`方法更新字段为默认值
   - 示例：
     ```java
     Book book = new Book();
     book.setToDefault("pages");
     book.updateAll();
     ```
   - 此代码将所有书的页数更新为默认值0

### 使用LitePal删除数据

1. **删除已存储对象**：
   - **方式一**：通过调用已存储对象的`delete()`方法删除数据。
   - 已存储对象的定义：
     - 通过调用`save()`方法添加的数据。
     - 通过LitePal提供的查询API获取的对象。

2. **使用deleteAll()方法删除数据**：
   - **方式二**：可以通过`DataSupport.deleteAll()`方法来删除符合条件的数据。
   - 语法示例：
     ```java
     DataSupport.deleteAll(Book.class, "price < ?", "15");
     ```
   - 第一个**参数**指定要删除的表（如`Book.class`）
   - 后面的**参数**用于指定删除的条件，例如：`"price < ?"`

3. **删除条件示例**：
   - 上述代码会删除Book表中价格低于15的书籍。
   - 如果不指定条件，则会删除表中的所有数据。

4. **代码示例**：
   ```java
   public class MainActivity extends AppCompatActivity {
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           Button deleteButton = (Button) findViewById(R.id.delete_data);
           deleteButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DataSupport.deleteAll(Book.class, "price < ?", "15");
               }
           });
       }
   }
   ```

5. **效果验证**：
   - 运行程序后点击“Delete data”按钮，查询表中的数据，确认价格低于15的书籍已被删除。

### 使用LitePal查询数据

1. **查询的简洁性**：
   - LitePal提供了简洁易用的查询API，避免了复杂的参数列表。
   - 例如，查询所有数据的简单方式：
     ```java
     List<Book> books = DataSupport.findAll(Book.class);
     ```

2. **遍历查询结果**：
   - `findAll` 查询结果为`Book`类型的List集合，可以直接遍历。
   - 示例代码：
     ```java
     public class MainActivity extends AppCompatActivity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                ...
                Button queryButton = (Button) findViewById(R.id.query data);
                queryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Book> books = DataSupport.findAll(Book.class);
                        for (Book book: books) {
                            Log.d("MainActivity", "book name is " + book.getName());
                            Log.d("MainActivity", "book author is " + book.getAuthor());
                            Log.d("MainActivity", "book pages is " + book.getPages());
                            Log.d("MainActivity", "book price is " + book.getPrice());
                            Log.d("MainActivity", "book press is " + book.getPress());
                        }
                    }
                });
            }
      }
     ```

3. **其他查询API**：
   - **查询第一条数据**：
     ```java
     Book firstBook = DataSupport.findFirst(Book.class);
     ```
   - **查询最后一条数据**：
     ```java
     Book lastBook = DataSupport.findLast(Book.class);
     ```

4. **连缀查询功能**：
   - **select()**：指定查询的列。
     ```java
     List<Book> books = DataSupport.select("name", "author").find(Book.class);
     ```
   - **where()**：指定查询的条件。
     ```java
     List<Book> books = DataSupport.where("pages > ?", "400").find(Book.class);
     ```
   - **order()**：指定结果的排序。
     ```java
     List<Book> books = DataSupport.order("price desc").find(Book.class);
     ```
   - **limit()**：指定查询结果的数量。
     ```java
     List<Book> books = DataSupport.limit(3).find(Book.class);
     ```
   - **offset()**：指定查询结果的偏移量。
     ```java
     List<Book> books = DataSupport.limit(3).offset(1).find(Book.class);
     ```

5. **组合查询示例**：
   - 组合多个查询条件的示例：
     ```java
     List<Book> books = DataSupport.select("name", "author", "pages")
                                    .where("pages > ?", "400")
                                    .order("pages")
                                    .limit(10)
                                    .offset(10)
                                    .find(Book.class);
     ```

6. **原生SQL查询**：
   - 在特殊情况下，可以使用原生SQL进行查询。
   - 示例代码：
     ```java
     Cursor c = DataSupport.findBySQL("select * from Book where pages > ? and price < ?", "400", "20");
     ```
