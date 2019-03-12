# URL Shortner project
This is sample project to describe the URL Shortner project system design with implementation.

**Required Tech-spec :**

	1. Java (v1.8 or later)
	2. Springboot  (v2.0.5.RELEASE)
	3. Eclipse 
	4. Maven 
	5. Redis (v4.0.8 64bit) 
	6. Apache Commons - commons-text (v1.6) 

**Classes, interfaces, entities and exceptions :**

	 - ApplicationStartup : Java class for spring boot application startup
	 - RestUrlShortner: Rest controller to handle events for URL Shortner
	 - UrlShortnerService: Service class with business logic for URL Shorting
	 - RedisRepository: Redis Repository class for cache specific operations
	 - UrlEntity: URL entity with request payload
	 - UrlResponse: Response entity class
	 - InvalidUrlException: Exception class if invalid URL received in request
	 - UrlNotFoundException: Exception class if URL doesn't available in cache
	 - UrlValidator: Validator class for validating URL

**Unique key generator**
    
I have used Apache's `common-text` library. This helps to generate the unique key alpha-numeric 8 character length key for url shortning.<br /> The same key is used to store and retrieve the url in Redis cache. The implementation in service class.

	private RandomStringGenerator generator = null;
	
	@Value("${com.makhir.url-key.length}")
	private int keyLength;
	
	public UrlShortnerService() {
		generator = new RandomStringGenerator.
					Builder()
					.withinRange('0', 'z')
			        .filteredBy(LETTERS, DIGITS).
			        build();
	}
	private String createUniqueId(){
		return generator.generate(keyLength);
	}


**Installing and Starting Redis Server on Mac**

For Redis --**RE**mote **DI**rectory **S**erver-- server setup, I have followed this nice [blog](https://medium.com/@petehouston/install-and-config-redis-on-mac-os-x-via-homebrew-eb8df9a4f298).

To start Redis server  

    redis-server /usr/local/etc/redis.conf

To check server is up or not.  

    redis-cli ping

Redis server connection details  

    URL: http://localhost:6379

**Build project**

    mvn clean build install

**Run project**

    mvn spring-boot:run

**API to short URL**

    POST http://localhost:9090/url/short

    Request Payload:  
    
    {
	    "actual-url":"https://github.com/akmishra30/system-design/tree/master/url-shortner"
    }

	Response Payload:
    {
        "shorten-url": "http://localhost:9090/AhFVEbyS",
        "created-date": "2019-03-11T11:25:48.337+0000"
    }

**API to list all the shortened URLs.**

    GET http://localhost:9090/url/list

**API to redirect to actual URL**

    GET http://localhost:9090/ErTJp4Re

**Entity to be saved in cache**

	@Data
	@RedisHash(value="url-entities", timeToLive=60*60*24*30) //1 month
	public class UrlEntity implements Serializable{
		@Id
		private String id;
		@JsonProperty(required=true, value="actual-url")
		@Indexed
		private String actualUrl;
		@JsonProperty(value="create-date")
		@Indexed
		private Date createDate;
	}
	
