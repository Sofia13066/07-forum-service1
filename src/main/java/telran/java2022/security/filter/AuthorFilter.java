package telran.java2022.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.post.dao.PostRepository;


@Order(40)
@RequiredArgsConstructor
@Component
public class AuthorFilter implements Filter {
  final UserAccountRepository userRepository;
  final PostRepository forumRepository;
    

@Override
public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain nexFilterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) arg0;
    HttpServletResponse response = (HttpServletResponse) arg1;
    String path = request.getServletPath();
    String method = request.getMethod();
    if (checkEndPoint(method, path)) {
      String user = request.getUserPrincipal().getName();
      String author = path.split("/")["POST".equalsIgnoreCase(method) ? 3 : 5];
      if (!user.equals(author)) {
        response.sendError(403, "Wrong user");
        return;
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  

  private boolean checkEndPoint(String method, String servletPath) {
    return ("POST".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/?")) ||
        ("PUT".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/comment/\\w+/?"));
  }
    
}

