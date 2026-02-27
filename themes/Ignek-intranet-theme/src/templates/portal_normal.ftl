<!DOCTYPE html>
<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${html_title}</title>
	<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	<@liferay_util["include"] page=top_head_include />

	<link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
      rel="stylesheet"
    />
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<@liferay.control_menu />

<header id="banner" role="banner">
	<#if !is_signed_in>
		<a data-redirect="${is_login_redirect_required?string}" href="${sign_in_url}" id="sign-in" rel="nofollow">${sign_in_text}</a>
	</#if>
</header>

<div class="container-fluid position-relative full-height-container" id="wrapper">
	<#assign user = themeDisplay.getUser() />
	<div class="row h-100">
		<div class="col-md-2 sidebar-column d-flex flex-column justify-content-between">
			<div>
				<div class="site-heading d-flex">
					<span class="line"></span>
					<span class="site-name poppins-bold">${site_name}</span>
					</div>
					<div class="user-profile d-flex flex-column align-items-center">
					<div class="img-elips">
						<img src="${user.getPortraitURL(themeDisplay)}" class="user-img" alt="user" />
					</div>
					<div class="mt-2">
						<span class="user-name poppins-bold">${user.fullName}</span>
					</div>
					<div>
						<span class="user-role poppins-medium">Employee</span>
					</div>
					</div>
					<div>
						<#if has_navigation && is_setup_complete>
							<#include "${full_templates_path}/navigation.ftl" />
						</#if>
					</div>
			</div>
			<div>
				<div
				class="logout-container d-flex align-items-center justify-content-center"
				>
					<a
					class="logout-container-a d-flex align-items-center justify-content-center"
					href="${theme_display.getURLSignOut()}"
					>
					<span class="poppins-medium mr-4">Logout</span>
					<img src="${images_folder}/sign-out.png" alt="logout" />
					</a>
				</div>
			</div>
		</div>
		<div class="col-md-10 main-content-column">
			<div
				class="top-search-container d-flex flex-row-reverse align-items-center"
			>
				<div class="bell-img-container">
				<img
					class="bell-img poppins-regular"
					src="${images_folder}/bell.png"
					alt="bell"
				/>
				</div>
				<div>
					<@liferay.search_bar  />
				</div>
			</div>
			<section id="content">
				<h2 class="hide-accessible sr-only" role="heading" aria-level="1">${htmlUtil.escape(the_title)}</h2>
				<#if selectable>
					<@liferay_util["include"] page=content_include />
				<#else>
					${portletDisplay.recycle()}

					${portletDisplay.setTitle(the_title)}

					<@liferay_theme["wrap-portlet"] page="portlet.ftl">
						<@liferay_util["include"] page=content_include />
					</@>
				</#if>
			</section>
		</div>
	</div>
</div>

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

<!-- inject:js -->
<!-- endinject -->

</body>

</html>