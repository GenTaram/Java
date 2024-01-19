<style>
  .navbar-purple {
    background-color: rgb(221, 160, 221);
  }
</style>
<nav class="main-header navbar navbar-expand navbar-purple navbar-light">
  <!-- Left navbar links -->
  <ul class="navbar-nav">
    <li class="nav-item">
      <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
    </li>
    <li class="nav-item d-none d-sm-inline-block">
      <a href="index3.html" class="nav-link">Home</a>
    </li>
    <li class="nav-item d-none d-sm-inline-block">
      <a href="#" class="nav-link">Contact</a>
    </li>
  </ul>

  <!-- Right navbar links -->
  <ul class="navbar-nav ml-auto">
    <!-- Navbar Search -->
    <li class="nav-item">
      <a class="nav-link" data-widget="navbar-search" href="#" role="button">
        <i class="fas fa-search"></i>
      </a>
      <div class="navbar-search-block">
        <form class="form-inline">
          <div class="input-group input-group-sm">
            <input class="form-control form-control-navbar" type="search" placeholder="Search" aria-label="Search">
            <div class="input-group-append">
              <button class="btn btn-navbar" type="submit">
                <i class="fas fa-search"></i>
              </button>
              <button class="btn btn-navbar" type="button" data-widget="navbar-search">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
        </form>
      </div>
    </li>
    <!--수정 부분-->

    <!-- Messages Dropdown Menu -->
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <c:choose>
      <c:when test="${dto == null}">
        <li class="nav-item dropdown">
          <a href="../members/login-form" class="nav-link">
            <i class="fas fa-sign-in-alt"></i>
          </a>
        </li>
        <!-- Notifications Dropdown Menu -->
        <li class="nav-item dropdown">
          <a class="nav-link" href="../members/post-form">
            <i class="fas fa-user-plus"></i>
          </a>
        </li>
      </c:when>
      <c:otherwise>
        <li class="nav-item dropdown">
          <a href="../members/detail?seq=${dto.mid}" class="nav-link">
            <i class="fas fa-user-edit"></i>
          </a>
        </li>
        <!-- Notifications Dropdown Menu -->
        <li class="nav-item dropdown">
          <a class="nav-link" href="../members/logout">
            <i class="fas fa-sign-out-alt"></i>
          </a>
        </li>

        <!-- Check if the user is admin and add 'list fa' accordingly -->
        <c:if test="${dto.email eq 'admin@induk.ac.kr'}">
          <li class="nav-item dropdown">
            <a class="nav-link" href="../members/get-list">
              <i class="fas fa-users"></i>
            </a>
          </li>
        </c:if>
      </c:otherwise>
    </c:choose>

    <li class="nav-item">
      <a class="nav-link" data-widget="fullscreen" href="#" role="button">
        <i class="fas fa-expand-arrows-alt"></i>
      </a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-widget="control-sidebar" data-controlsidebar-slide="true" href="#" role="button">
        <i class="fas fa-th-large"></i>
      </a>
    </li>
  </ul>
</nav>
