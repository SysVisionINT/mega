<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="http://java.sun.com/jstl/core"   prefix="c" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<TABLE BORDER="1">
	<TR>
		<TD><a:key key="list.label.id"/></TD>
		<TD><a:key key="list.label.name"/></TD>
		<TD><a:key key="list.label.admin"/></TD>
		<TD>&nbsp;</TD>
	</TR>
<c:forEach var="elemento" items="${this.recordList}" varStatus="forEachStatus1">
	<TR>
		<TD><c:out value="${elemento.id}"/></TD>
		<TD><c:out value="${elemento.name}"/></TD>
		<TD><c:out value="${elemento.admin}"/></TD>
		<TD>[
			<t:link method="delete">
				<t:parameter name="id">
					<c:out value="${elemento.id}"/>
				</t:parameter>
				<a:key key="list.label.delete"/>
			</t:link>
			]</TD>
	</TR>
</c:forEach>
</TABLE>

<BR/>

<t:link method="add"><a:key key="list.label.addnew"/></t:link>


<BR/>
<BR/>
<a:key key="list.label.lastchange"/> <a:value property="lastChange"/>
<BR>
<BR>
<BR>
<t:link action="/currentLocate"><a:key key="list.label.locate"/></t:link>
