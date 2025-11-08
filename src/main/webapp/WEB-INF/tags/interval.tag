<%@ tag description="Interval Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="interval" required="true" type="ru.itis.example.models.DurationWrapper" %>

<div class="intervals_div">
  <label>${interval.getRussianName()}</label>

  <div class="interval_parts">
    <div class="interval_part">
      <input name="${interval.getName()}_days" value="${interval.getDays()}" type="number" min="0" max="365">
      <p>Дни</p>
    </div>

    <div class="interval_part">
      <input name="${interval.getName()}_hours" value="${interval.getHours()}" type="number" min="0" max="23">
      <p>Часы</p>
    </div>

    <div class="interval_part">
      <input name="${interval.getName()}_minutes" value="${interval.getMinutes()}" type="number" min="0" max="59">
      <p>Минуты</p>
    </div>

    <div class="interval_part">
      <input name="${interval.getName()}_seconds" value="${interval.getSeconds()}" type="number" min="0" max="59">
      <p>Секунды</p>
    </div>
  </div>
</div>
