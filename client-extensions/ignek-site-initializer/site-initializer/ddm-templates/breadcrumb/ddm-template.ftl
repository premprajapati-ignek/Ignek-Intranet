<div class="intranet-banner">
    <#if entries?has_content>
        <ol class="breadcrumb poppins-medium">
            <#list entries as entry>
                <li class="breadcrumb-item">
                    <#if entry?has_next>
                        <a class="breadcrumb-link"
                           <#if entry.isBrowsable()>
                               href="${htmlUtil.escapeAttribute(entry.getURL()!"")}"
                           </#if>>
                            <span class="breadcrumb-text-truncate">
                                ${htmlUtil.escape(entry.getTitle())}
                            </span>
                        </a>
                    <#else>
                        <span aria-current="page"
                              class="active breadcrumb-text-truncate">
                            ${htmlUtil.escape(entry.getTitle())}
                        </span>
                    </#if>
                </li>
            </#list>
        </ol>
    </#if>
</div>

<style>
.intranet-banner .breadcrumb {
	background-color: white;
	padding: 0 0.6rem;
	margin: 0;
}
.intranet-banner .breadcrumb-link{
    color: #000;
}
.intranet-banner .breadcrumb .breadcrumb-item .active {
	color: #00979E;
	font-weight: 500;
}
</style>