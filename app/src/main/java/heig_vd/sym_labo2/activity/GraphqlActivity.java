package heig_vd.sym_labo2.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import heig_vd.sym_labo2.model.Authors;
import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.model.Post;
import heig_vd.sym_labo2.utils.Utils;


public class GraphqlActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner authorSpinner;
    private TextView authorsChoice;
    private TextView authorsPosts;
    private ListView postsList;

    private List<Authors> authorsNameList;
    private List<Post> postFromAuthorList;
    //Maybe we can use only one request
    private AsyncSendRequest requestAuthor;
    private AsyncSendRequest requestPosts;

    private final String graphQLAuthorsRequest = "{\"query\": \"{allAuthors{id first_name last_name}}\"}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle("GraphQL Activity");

        authorsChoice = (TextView) findViewById(R.id.authorsChoice);
        authorsChoice.setText(R.string.authorsChoice);

        authorsPosts = (TextView) findViewById(R.id.authorPosts);
        authorsPosts.setText(R.string.authorsPosts);

        authorSpinner = (Spinner) findViewById(R.id.authorsSpinner);
        authorSpinner.setOnItemSelectedListener(this);

        postsList = (ListView) findViewById(R.id.postList);

        requestPosts = new AsyncSendRequest(GraphqlActivity.this);

        try {
            callForAuthors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callForAuthors() throws Exception{
        //first request.
        requestAuthor = new AsyncSendRequest(GraphqlActivity.this);
        requestAuthor.sendGraphQLRequest(graphQLAuthorsRequest, Utils.GRAPHQL_URL);
        requestAuthor.setCommunicationEventListener(response -> {
            if(response != null){
                try{
                    //authorsNameList =
                    getAuthorsName(response);
                    authorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, authorsNameList));
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        });
    }

    private void getAuthorsName(String response) {
        //Async request to fetch all authors and populate the dropdown menu
        //List<Authors> authorsName =
        authorsNameList= new ArrayList<>();
        try {
            JSONArray authorsArray = new JSONObject(response)
                                                .getJSONObject("data")
                                                .getJSONArray("allAuthors");
            JSONObject author;
            String name;
            int size = authorsArray.length();
            for(int i = 0; i < size; i++ ) {
                author = authorsArray.getJSONObject(i);
                name = author.getString("first_name") + " " + author.getString("last_name");
                authorsNameList.add(new Authors(author.getInt("id"), name));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return authorsName;
    }


    private void populateCustomList(String response) {
        getAuthorsPosts(response);
        ArrayAdapter<Post> postsArrayAdapter = new ArrayAdapter<>(GraphqlActivity.this, android.R.layout.simple_list_item_1, postFromAuthorList);
        postsList.setAdapter(postsArrayAdapter);
    }


    private void getAuthorsPosts(String response){
        postFromAuthorList = new ArrayList<>();

        try{
            JSONArray postsArray = new JSONObject(response)
                                            .getJSONObject("data")
                                            .getJSONObject("author")
                                            .getJSONArray("posts");
            JSONObject post;
            Log.e("POST 1:", postsArray.getJSONObject(0).getString("title"));
            int size = postsArray.length();
            for(int i = 0; i < size; i++ ) {
                post = postsArray.getJSONObject(i);
                postFromAuthorList.add(new Post(post.getString("title"), post.getString("content")));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }



    /* Methods to handle the spinner selection */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        int authorId = ((Authors) adapterView.getItemAtPosition(pos)).getId();
        String req = String.format("{\"query\": \"{author(id: %d){posts{title content}}}\"}", authorId);

        Toast.makeText(this, authorId + " "+ adapterView.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();

        requestPosts.sendGraphQLRequest(req, Utils.GRAPHQL_URL);
        requestPosts.setCommunicationEventListener(response -> {
            if(response != null){
                try{
                    Log.e("POST", response);
                    populateCustomList(response);
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        });

        //new request
        //then handle authors posts treatment

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
