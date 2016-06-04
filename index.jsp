<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
    CS 137
    Project 3
    Group 1

    index.jsp
-->

<html>
    <jsp:include page="/htmlhead?title=Home" flush="true" />
    <body onload="window_onload();">
        <div id="header">
            <h1 id="title">JavaSipt</h1>
        </div>
        <div id="nav" class="nav_rel">
            <ul>
                <li><a class="active" href="index.jsp">Home</a></li><!-- display inline block :(
             --><li><a href="/project3/viewcart">View Cart</a></li>
            </ul>
        </div>
        <div id="firstcontent" class="content">
            <jsp:include page="/products" flush="true"/>
            <jsp:include page="/recent" flush="true"/>
        </div>
    </body>
</html>
