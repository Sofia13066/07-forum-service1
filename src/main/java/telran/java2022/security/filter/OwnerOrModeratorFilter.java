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
import telran.java2022.accounting.model.UserAccount;
import telran.java2022.accounting.utils.Role;
import telran.java2022.post.dao.PostRepository;
import telran.java2022.post.model.Post;

@Order(30)
@RequiredArgsConstructor
@Component
public class OwnerOrModeratorFilter implements Filter {
  final UserAccountRepository userRepository;
  final PostRepository forumRepository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      String userId = request.getUserPrincipal().getName();
      UserAccount user = userRepository.findById(userId).get();
      if (!("DELETE".equalsIgnoreCase(request.getMethod()) && user.getRoles().contains(Role.MODERATOR))) {
        String postId = request.getServletPath().split("/")[3];
        Post post = forumRepository.findById(postId).orElse(null);
        if (post == null) {
          response.sendError(404);
          return;
        }
        if (!post.getAuthor().equalsIgnoreCase(userId)) {
          response.sendError(403);
          return;
        }
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return (("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
        && servletPath.matches("/forum/post/\\w+/?"));
  }
}
