package messengerServer.API.Request;

import messengerServer.API.Response.AuthorizationResponse;
import messengerServer.Application;
import messengerServer.DbHandler;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/createBot")
public class CreateBot{



    @PostMapping
    public AuthorizationResponse doTask(@RequestParam("name") String name) {
        if (!name.contains("_bot")){
            return new AuthorizationResponse(Application.FAILED_STATUS, 409, "");
        }
        try {
            DbHandler dbHandler = DbHandler.getInstance();

            String result = dbHandler.registerUser(name,"");
            if (result.equals(Application.OK_CODE))
                return new AuthorizationResponse(Application.SUCCESS_STATUS, 200, dbHandler.getUserByLogin(name).userToken);
            else
                return new AuthorizationResponse(result, 409, "");
        } catch (SQLException e) {
            return new AuthorizationResponse(Application.FAILED_STATUS, 500, "");
        }
    }


}



