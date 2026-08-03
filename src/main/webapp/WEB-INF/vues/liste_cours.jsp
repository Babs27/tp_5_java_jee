<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <span class="text-muted small text-uppercase">Supdeco &middot; Scolarité</span>
        <h1 class="mb-0">Catalogue des cours</h1>
        <small class="text-muted">
            ${totalCours} cours enregistré<c:if test="${totalCours > 1}">s</c:if>
            &middot; page ${pageCourante} sur ${nombrePages}
        </small>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/cours?action=nouveau">
        <i class="bi bi-plus-circle"></i> Ajouter un cours
    </a>
</div>


<div class="card mb-4 shadow-sm">
    <div class="card-body py-2">
        <div class="input-group">
            <span class="input-group-text bg-white border-end-0"><i class="bi bi-search text-muted"></i></span>
            <input type="text" id="rechercheCours" class="form-control border-start-0 ps-0" placeholder="Filtrer par code, intitulé ou enseignant...">
        </div>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <c:choose>
            <c:when test="${empty listeCours}">
                <div class="p-4 text-center text-muted">
                    <p class="mb-0">Aucun cours sur cette page.<br>Utilisez le bouton &laquo; Ajouter un cours &raquo; pour en créer un.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <%-- 3. Tableau Bootstrap avec rayures alternées --%>
                    <table class="table table-striped table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>Code</th>
                            <th>Intitulé</th>
                            <th>Crédits</th>
                            <th>Enseignant</th>
                            <th class="text-end">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cours" items="${listeCours}">
                            <tr class="ligne-cours">
                                <td class="align-middle code-cours"><strong><c:out value="${cours.code}"/></strong></td>
                                <td class="align-middle intitule-cours"><c:out value="${cours.intitule}"/></td>
                                <td class="align-middle">
                                    <%-- 3. Badges colorés pour les crédits --%>
                                    <c:choose>
                                        <c:when test="${cours.credits >= 3}">
                                            <span class="badge bg-success">${cours.credits} cr.</span>
                                        </c:when>
                                        <c:when test="${cours.credits == 2}">
                                            <span class="badge bg-warning text-dark">${cours.credits} cr.</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">${cours.credits} cr.</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="align-middle enseignant-cours"><c:out value="${cours.enseignant}"/></td>
                                <td class="text-end align-middle">
                                    <%-- 3. Boutons d'action avec Bootstrap Icons --%>
                                    <a class="btn btn-sm btn-outline-info me-1" title="Modifier"
                                       href="${pageContext.request.contextPath}/cours?action=modifier&amp;id=${cours.id}&amp;page=${pageCourante}">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a class="btn btn-sm btn-outline-danger" title="Supprimer"
                                       href="${pageContext.request.contextPath}/cours?action=supprimer&amp;id=${cours.id}&amp;page=${pageCourante}"
                                       onclick="return confirm('Supprimer le cours ${fn:escapeXml(cours.code)} ?');">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>


<c:if test="${nombrePages > 1}">
    <nav class="mt-4" aria-label="Pagination des cours">
        <ul class="pagination justify-content-center">
            <li class="page-item ${pageCourante <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/cours?action=liste&amp;page=${pageCourante - 1}">&lsaquo; Précédent</a>
            </li>
            <c:forEach var="i" begin="1" end="${nombrePages}">
                <li class="page-item ${i eq pageCourante ? 'active' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/cours?action=liste&amp;page=${i}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${pageCourante >= nombrePages ? 'disabled' : ''}">
                <a class="page-link" href="${pageContext.request.contextPath}/cours?action=liste&amp;page=${pageCourante + 1}">Suivant &rsaquo;</a>
            </li>
        </ul>
    </nav>
</c:if>


<script>
document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById("rechercheCours");
    if(searchInput) {
        searchInput.addEventListener("keyup", function() {
            const filter = searchInput.value.toLowerCase();
            const tableRows = document.querySelectorAll(".ligne-cours");

            tableRows.forEach(function(row) {
                const textCode = row.querySelector(".code-cours").textContent.toLowerCase();
                const textIntitule = row.querySelector(".intitule-cours").textContent.toLowerCase();
                const textEnseignant = row.querySelector(".enseignant-cours").textContent.toLowerCase();

                if (textCode.includes(filter) || textIntitule.includes(filter) || textEnseignant.includes(filter)) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            });
        });
    }
});
</script>

<%@ include file="footer.jsp" %>