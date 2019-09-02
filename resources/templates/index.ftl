<#-- @ftlvariable name="data" type="fan.zheyuan.ktor-exposed.IndexData" -->
<html>
    <body>
        <ul>
        <#list data.items as item>
            <li>${item}</li>
        </#list>
        </ul>
    </body>
</html>
