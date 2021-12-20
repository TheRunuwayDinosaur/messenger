package messengerServer.API.Request;

import messengerServer.API.Response.AuthorizationResponse;
import messengerServer.API.Response.BaseResponse;
import messengerServer.API.Response.GetMessagesResponse;
import messengerServer.Application;
import messengerServer.Chat;
import messengerServer.DbHandler;
import messengerServer.User;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.BaseStream;

@RestController
@RequestMapping("/addUserToChat")
public class AddUserToChat {

    @PostMapping
    public BaseResponse doTask(@RequestParam("token") String user_token, @RequestParam("chatId") String chatId,
                               @RequestParam("userName") String userName) {

        try {
            DbHandler dbHandler = DbHandler.getInstance();
            User user = dbHandler.getUserByToken(user_token);
            if (user == null)
                return new BaseResponse("token error", 409);
            if (dbHandler.getUserByLogin(userName) == null){
                return new BaseResponse(Application.FAILED_STATUS,409);
            }
            Chat chat = dbHandler.getChatById(chatId);
            if (chat == null)
                return new BaseResponse("chat id error", 409);
            if (dbHandler.addUserToChat(chat,userName) == Application.OK_CODE) {
                return new BaseResponse(Application.SUCCESS_STATUS, 200);
            }
            return new BaseResponse(Application.FAILED_STATUS,409);

        } catch (SQLException e) {
            e.printStackTrace();
            return new BaseResponse(Application.FAILED_STATUS, 500);
        }
    }


}

