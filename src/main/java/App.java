import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.j_scholl.jgraphql.JGQLProperty;
import com.tumblr.j_scholl.jgraphql.SchemaBuilder;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.request.RequestBuilder;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example usage of Jumblr
 * 
 * @author jc
 */
public class App {
	//private final static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args)
			throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {

		// Read in the JSON data for the credentials
		BufferedReader br = new BufferedReader(new FileReader("credentials.json"));
		
		String jsonString = br.lines().collect(Collectors.joining());
		br.close();
		
		/*StringBuilder json = new StringBuilder();
		try {
			while (br.ready()) {
				json.append(br.readLine());
			}
		} finally {
			br.close();
		}*/

		// Parse the credentials
		JsonObject obj = (JsonObject) new JsonParser().parse(jsonString);

		// Create a client, give it a token
		JumblrClient client = new JumblrClient(
				obj.getAsJsonPrimitive("consumer_key").getAsString(),
				obj.getAsJsonPrimitive("consumer_secret").getAsString(),
				obj.getAsJsonPrimitive("oauth_token").getAsString(),
				obj.getAsJsonPrimitive("oauth_token_secret").getAsString());

		new App(client).graphQLTest();
	}

	private final JumblrClient client;

	public App(JumblrClient client) {
		this.client = client;
	}

	private void graphQLTest() {
		SchemaBuilder builder = new SchemaBuilder(this::getDataFetcher);
		builder.addAll(Post.class, Blog.class, User.class, RequestBuilder.class, JumblrClient.class);

		GraphQLSchema schema = builder.finalize(JumblrClient.class);

		System.out.println(client.user().getName());
		Map<String, Object> result = new GraphQL(schema)
				.execute("{user {name, blogs {name, description}}}", client).getData();
		System.out.println(result);
		
		/*Map<String, Object> options = new HashMap<>();
		options.put("tag", null);
		System.out.println(client.blogPosts("yo-fuckers", options));*/
	}

	private DataFetcher getDataFetcher(Method m) {
		// System.out.println("returning datafetcher");
		return (de -> this.dataFetch(m, de));
	}

	private Object dataFetch(Method m, DataFetchingEnvironment de) {
		String propName = getPropertyName(m);
		DataFetcher base = new PropertyDataFetcher(propName);

		/*System.out.printf("Fetching property %s of class %s, with source %s and context %s%n", propName,
				m.getDeclaringClass().getSimpleName(), de.getSource(), de.getContext());*/

		/*System.out.printf("Declaring Class: %s%n", m.getDeclaringClass().getSimpleName());
		System.out.printf("Property Name: %s%n", propName);
		System.out.printf("Source: %s%n", de.getSource());
		System.out.printf("Context: %s%n", de.getContext());
		System.out.printf("Fields: %s%n", de.getFields());
		System.out.println();*/
		assert de.getParentType().getName().equals(m.getDeclaringClass().getSimpleName());
		assert de.getSource() == null || de.getSource().getClass().equals(m.getDeclaringClass());

		if (de.getSource() == null && m.getDeclaringClass().equals(JumblrClient.class)) {
			de = new DataFetchingEnvironment(de.getContext(), de.getArguments(), de.getContext(), de.getFields(),
					de.getFieldType(), de.getParentType(), de.getGraphQLSchema());
		}

		if (m.isAnnotationPresent(JGQLProperty.class)) {
			System.out.println("trying to invoke");
			try {
				return m.invoke(de.getSource());
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		// if (de.getSource().getClass().equals(JumblrClient.class))

		// System.out.printf("Declaring Class: %s%nFields: %s%nParent: %s%nProperty Name: %s%n",
		// m.getDeclaringClass().getSimpleName(), de.getFields(), de.getParentType(), name);
		// return /*name.equals("name") ? "jim" : */name.equals("hero") ? GarfieldSchema.john : base.get(de);

		return base.get(de);
	}

	private static String getPropertyName(Method m) {
		if (m.isAnnotationPresent(JGQLProperty.class))
			return m.getAnnotation(JGQLProperty.class).value();
		String s = m.getName();
		assert s.startsWith("get") || s.startsWith("is");
		s = s.replaceAll("^get", "").replaceAll("^is", "");
		s = s.substring(0, 1).toLowerCase().concat(s.substring(1));
		return s;
	}

	private Object getPropertyViaGetter(Object object, GraphQLOutputType outputType, String propertyName) {
		String prefix = outputType == Scalars.GraphQLBoolean ? "is" : "get";
		String getterName = prefix + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		try {
			Method method = object.getClass().getMethod(getterName);
			return method.invoke(object);

		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
