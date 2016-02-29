<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html ng-app="app-test">
   <head>
     <meta charset=utf-8> 
     <title>Flight Search</title> 
     <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
     <link rel=stylesheet href="http://fonts.googleapis.com/icon?family=Material+Icons"/>
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
     <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
     <meta name="viewport" content="width=device-width, initial-scale=1">
     <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
     <link rel=stylesheet href="<c:url value="/resources/css/style.css"/>">
     <script src="<c:url value="resources/js/controller.js" />"></script>
   </head>
   <body ng-controller="entryCtrl">
     <nav class="book">
	   <p>Flights</p>
     </nav> 
     <form class="form-horizontal" role="form" method="post" action="find">
         <div class="form-group">
            <div class="col-sm-6">
             <input type="text" class="form-control" name="originname" placeholder="Airport/Port" ng-model="getOrigin" required>
            </div>
            <div class="col-sm-6">
			 <input type="text" placeholder="Airport/Port" name="destinationname" ng-model="getDestination" class="form-control" required>
			</div>
	     </div>
	    <div class="form-group">
	      <div class="col-sm-6">
			 <input type="number" placeholder="Number of Adults" name="adult" min="1" ng-model="getAdult" class="form-control" required>
		  </div>
		  <div class="col-sm-6">
			 <input type="number" placeholder="Number of Children" name="child" min="0" ng-model="getChild" class="form-control" required>
	      </div>
	    </div>
	    <div class="form-group">
	         <div class="col-sm-2">
	         <label>Transfer Date</label>&nbsp;&nbsp;<span class="glyphicon glyphicon-calendar"></span>
	         </div>
	         <div class="col-sm-4">
	         <input type="date" placeholder="Transfer Date" name="tdate" ng-model="getTranferDate" min="{{date|date:'yyyy-MM-dd'}}" class="form-control" requied>
	         </div>
	         <div class="col-sm-3">
	         <input type="time" placeholder="Arrival Time" name="arrivaltime" ng-model="getArrivalTime" class="form-control" required>
	         </div>
	         <div class="col-sm-3">
	         <input type="submit" id="sub" value="Search Flights" class="btn btn-primary">
	         </div>
        </div> 
     </form>
     
     <div>
     <table class="table table-hover">
      <c:forEach items="${durationLeg}" var="frien">
      <tr>
        <td><h3>${flightCarrier.pop()}${flightNum.pop()}</h3></td>
        <td><h3>${Price.pop()}</h3></td>
      </tr>
      <tr>
        <td><span class="glyphicon glyphicon-time"></span>${frien}&nbsp;minutes</td>
        <td>per person</td>
      </tr>
      <tr>
        <td><h3>${origin.pop()}&nbsp;&nbsp;${departTime.pop()}&nbsp; TO &nbsp;${dest.pop()}&nbsp;&nbsp;${arrivalTime.pop()}</h3></td>
        <td><button class="btn btn-primary">ADD TO PACKAGE</button></td>
      </tr>
      </c:forEach>
    </table>
    </div>
  </body>
</html>
