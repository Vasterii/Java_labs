package bsu.rfe.java.group7.lab8.khomenko.varA4.servlet;

import bsu.rfe.java.group7.lab8.khomenko.varA4.entity.ChatMessage;
import bsu.rfe.java.group7.lab8.khomenko.varA4.entity.ChatUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected HashMap<String, ChatUser> activeUsers;    // Карта текущих пользователей
    protected ArrayList<ChatMessage> messages; // Список сообщений чата


    @SuppressWarnings("unchecked")
    public void init() throws ServletException {

        super.init(); // Вызвать унаследованную от HttpServlet версию init()

        // Извлечь из контекста карту пользователей и список сообщений
        activeUsers = (HashMap<String, ChatUser>) getServletContext().getAttribute("activeUsers");
        messages = (ArrayList<ChatMessage>) getServletContext().getAttribute("messages");

        // Если карта пользователей не определена ...
        if (activeUsers==null) {
            // Создать новую карту
            activeUsers = new HashMap<String, ChatUser>();
            // Поместить ее в контекст сервлета, чтобы другие сервлеты могли до него добраться
            getServletContext().setAttribute("activeUsers", activeUsers);
        }

        // Если список сообщений не определѐн ...
        if (messages==null) {
            // Создать новый список
            messages = new ArrayList<ChatMessage>(100);
            // Поместить его в контекст сервлета, чтобы другие сервлеты могли до него добрать
            getServletContext().setAttribute("messages", messages);
        }
    }
}

