<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8 col-lg-6">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <span class="text-muted small text-uppercase">Supdeco &middot; Scolarité</span>
                <h1 class="mb-0"><c:out value="${titrePage}"/></h1>
                <c:if test="${not cours.nouveau}">
                    <span class="badge bg-secondary mt-2">Fiche n&deg;${cours.id}</span>
                </c:if>
            </div>
        </div>


        <c:if test="${not empty erreurs}">
            <div class="alert alert-danger shadow-sm">
                <strong>Le cours n'a pas pu être enregistré :</strong>
                <ul class="mb-0 mt-2">
                    <c:forEach var="erreur" items="${erreurs}">
                        <li><c:out value="${erreur}"/></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <div class="card shadow-sm">
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/cours" class="needs-validation">

                    <input type="hidden" name="action" value="sauvegarder">
                    <input type="hidden" name="id" value="${cours.id}">
                    <input type="hidden" name="page" value="${empty param.page ? 1 : param.page}">

                    <div class="row mb-3">
                        <div class="col-md-8">
                            <label for="code" class="form-label fw-bold">Code</label>
                            <input type="text" class="form-control" id="code" name="code"
                                   maxlength="10" required
                                   value="<c:out value='${cours.code}'/>" placeholder="Ex: INF202">
                            <div class="form-text">Identifiant unique, 10 caractères max.</div>
                        </div>

                        <div class="col-md-4">
                            <label for="credits" class="form-label fw-bold">Crédits</label>
                            <input type="number" class="form-control" id="credits" name="credits"
                                   min="1" max="30" required
                                   value="${cours.credits == 0 ? '' : cours.credits}" placeholder="6">
                            <div class="form-text">Entre 1 et 30.</div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="intitule" class="form-label fw-bold">Intitulé</label>
                        <%-- 4. Validation côté client (required + minlength) --%>
                        <input type="text" class="form-control" id="intitule" name="intitule"
                               required minlength="3"
                               value="<c:out value='${cours.intitule}'/>"
                               placeholder="Ex: Développement Web JEE">
                        <div class="form-text">Minimum 3 caractères.</div>
                    </div>

                    <div class="mb-4">
                        <label for="enseignant" class="form-label fw-bold">Enseignant</label>
                        <%-- 4. Validation côté client (required + minlength) --%>
                        <input type="text" class="form-control" id="enseignant" name="enseignant"
                               required minlength="3"
                               value="<c:out value='${cours.enseignant}'/>"
                               placeholder="Ex: M. Fall">
                    </div>

                    <div class="d-flex justify-content-end gap-2">
                        <a class="btn btn-light border" href="${pageContext.request.contextPath}/cours?action=liste&amp;page=${param.page}">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-save me-1"></i> ${cours.nouveau ? 'Ajouter le cours' : 'Enregistrer'}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>