<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--
    Point d'entree de l'application.
    Tout passe par le controleur : on redirige immediatement vers /cours.
--%>
<% response.sendRedirect(request.getContextPath() + "/cours?action=liste"); %>
