package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class GameController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public Result result() {
        JsonNode json = request().body().asJson();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String box = json.findPath("box").toString();


            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(box);

                Map<String,String> result = new HashMap<>();

                result.put("message","Failed");

                for(int x = 0;x<node.size();x++){

                    /*
                     * Column Check
                     */
                    boolean columnCheck = false;
                    for(int y = 0;y<node.size()-1;y++){
                        if(node.get(x).get(y).toString().equals(node.get(x).get(y + 1).toString()) && !node.get(x).get(y).toString().equals("\"+\"")){
                            result.put("win",node.get(x).get(y).toString().replace("\"",""));
                            columnCheck = true;
                        }else{
                            columnCheck = false;
                            break;
                        }
                    }

                    if(columnCheck){
                        result.put("message","Success");
                        return ok(Json.toJson(result));
                    }

                    /*
                     * Row Check
                     */
                    boolean rowCheck = false;
                    for(int y = 0;y<node.size()-1;y++){
                        if(node.get(y).get(x).toString().equals(node.get(y+1).get(x).toString()) && !node.get(y).get(x).toString().equals("\"+\"")){
                            result.put("win",node.get(y).get(x).toString().replace("\"",""));
                            rowCheck = true;
                        }else{
                            rowCheck = false;
                            break;
                        }
                    }

                    if(rowCheck){
                        result.put("message","Success");
                        return ok(Json.toJson(result));
                    }
                }

                /*
                 * Left Diagonal Check
                 */
                boolean rightDiagonalCheck = false;
                for(int x = 0;x<node.size()-1;x++){
                    if(node.get(x).get(x).toString().equals(node.get(x+1).get(x+1).toString()) && !node.get(x).get(x).toString().equals("\"+\"")){
                        result.put("win",node.get(x).get(x).toString().replace("\"",""));
                        rightDiagonalCheck = true;
                    }else{
                        rightDiagonalCheck = false;
                        break;
                    }
                }

                if (rightDiagonalCheck){
                    result.put("message","Success");
                    return ok(Json.toJson(result));
                }

                /*
                 * Right Diagonal Check
                 */
                boolean leftDiagonalCheck = false;
                int columnCount = node.size()-1;
                for(int x = 0;x<node.size()-1;x++){
                    if(node.get(x).get(columnCount).toString().equals(node.get(x+1).get(columnCount-1).toString()) && !node.get(x).get(columnCount).toString().equals("\"+\"")){
                        result.put("win",node.get(x).get(columnCount).toString());
                        leftDiagonalCheck = true;
                    }else{
                        leftDiagonalCheck = false;
                        break;
                    }
                    columnCount--;
                }

                if (leftDiagonalCheck){
                    result.put("message","Success");
                    return ok(Json.toJson(result));
                }

                /*
                 * Tie Check
                 */
                for(int x = 0;x<node.size();x++){
                    for (int y = 0;y<node.size();y++){
                        if(node.get(x).get(y).toString().equals("\"+\"")){
                            result.remove("win");
                            return ok(Json.toJson(result));
                        }
                    }
                }

                result.put("message","Success");
                result.put("win", "Tie");
                return ok(Json.toJson(result));

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(box == null) {
                return badRequest("Missing parameter [box]");
            } else {
                return ok("Hello " + box);
            }
        }
    }

}
