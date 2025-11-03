<%@ tag description="Interval Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="interval" required="true" type="ru.itis.example.models.DurationWrapper" %>

<div id="intervals_div">
  <label>${interval.getRussianName()}</label>

  <div class="interval_part">
    <input name="${interval.getName()}_days" value="${interval.getDays()}" type="number">
    <p>Дни</p>
  </div>

  <div class="interval_part">
  <input name="${interval.getName()}_hours" value="${interval.getHours()}" type="number">
  <p>Часы</p>
  </div>

  <div class="interval_part">
  <input name="${interval.getName()}_minutes" value="${interval.getMinutes()}" type="number">
  <p>Минуты</p>
  </div>

  <div class="interval_part">
    <input name="${interval.getName()}_seconds" value="${interval.getSeconds()}" type="number">
    <p>Секунды</p>
  </div>
</div>
