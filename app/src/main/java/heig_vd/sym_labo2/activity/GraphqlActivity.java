package heig_vd.sym_labo2.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import heig_vd.sym_labo2.communication.AsyncRequestOperation;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.model.Authors;
import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.model.Post;
import heig_vd.sym_labo2.utils.Utils;

public class GraphqlActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner authorSpinner;
    private ListView postsList;

    private List<Authors> authorsNameList;
    private List<Post> postFromAuthorList;

    private static final String graphQLAuthorsRequest = "{\"query\": \"{allAuthors{id first_name last_name}}\"}";
    private static final String graphQLOneAuthorRequest = "{\"query\": \"{author(id: %d){posts{title content}}}\"}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle("GraphQL Activity");

        /* Initialisation */
        TextView authorsChoice  = findViewById(R.id.authorsChoice);
        TextView authorsPosts   = findViewById(R.id.authorPosts);
        authorSpinner           = findViewById(R.id.authorsSpinner);
        postsList               = findViewById(R.id.postList);

        authorsChoice.setText(R.string.authorsChoice);
        authorsPosts.setText(R.string.authorsPosts);
        authorSpinner.setOnItemSelectedListener(this);

        try {
            callForAuthors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callForAuthors() {
        //first request.
        AsyncSendRequest asyncSendRequest = new AsyncSendRequest(GraphqlActivity.this, new AsyncRequestOperation(response -> {
            if(response != null){
                try{
                    getAuthorsName(response);
                    authorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, authorsNameList));
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }));
        asyncSendRequest.sendRequest(graphQLAuthorsRequest, Utils.GRAPHQL_URL);
    }

    /***
     *
     * @param response
     */
    private void getAuthorsPosts(String response) {
        postFromAuthorList = new ArrayList<>();

        try{
            JSONArray postsArray = new JSONObject(response)
                    .getJSONObject("data")
                    .getJSONObject("author")
                    .getJSONArray("posts");
            JSONObject post;
            int size = postsArray.length();
            for(int i = 0; i < size; i++ ) {
                post = postsArray.getJSONObject(i);
                postFromAuthorList.add(new Post(post.getString("title"), post.getString("content")));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /***
     *
     * @param response
     */
    private void getAuthorsName(String response) {
        authorsNameList= new ArrayList<>();
        try {
            JSONArray authorsArray = new JSONObject(response)
                                                .getJSONObject("data")
                                                .getJSONArray("allAuthors");
            JSONObject author;
            String firstName, lastName;
            int size = authorsArray.length();
            for(int i = 0; i < size; i++ ) {
                author = authorsArray.getJSONObject(i);
                firstName = author.getString("first_name");
                lastName = author.getString("last_name");
                authorsNameList.add(new Authors(author.getInt("id"), firstName, lastName));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /***
     * @brief populate the listView to display all posts of an author
     * @param response
     */
    private void populateCustomList(String response) {
        getAuthorsPosts(response);
        ArrayAdapter<Post> postsArrayAdapter = new ArrayAdapter<>(GraphqlActivity.this, android.R.layout.simple_list_item_1, postFromAuthorList);
        postsList.setAdapter(postsArrayAdapter);
    }

    /***
     * @brief function called when an item is selected from the spinner
     * @param adapterView
     * @param view
     * @param pos
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        int authorId = ((Authors) adapterView.getItemAtPosition(pos)).getId();
        String req = String.format(graphQLOneAuthorRequest, authorId);

        AsyncSendRequest asyncSendRequest = new AsyncSendRequest(GraphqlActivity.this,
                new AsyncRequestOperation(response -> {
            if(response != null){
                try{
                    populateCustomList(response);
                }catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }));
        asyncSendRequest.sendRequest(req, Utils.GRAPHQL_URL);
    }

    /****
     * @brief method called when nothing is selected in the spinner
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
