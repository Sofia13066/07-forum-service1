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

import lombok.AllArgsConstructor;

import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.accounting.model.UserAccount;
import telran.java2022.accounting.utils.Role;
import telran.java2022.post.dao.PostRepository;

@Component
@AllArgsConstructor
@Order(50)
public class UserOwnerOrAdminOwnerFilter implements Filter {
  final UserAccountRepository userRepository;
  final PostRepository forumRepository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      UserAccount user = userRepository.findById(request.getUserPrincipal().getName()).get();
      if (!("DELETE".equalsIgnoreCase(request.getMethod()) && user.getRoles().contains(Role.ADMIN))) {
        String login = request.getServletPath().split("/")[3];
        if (!(login.equals(request.getUserPrincipal().getName()))) {
          response.sendError(403);
          return;
        }
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return (("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
        && servletPath.matches("/account/user/\\w+/?"));
  }

}
