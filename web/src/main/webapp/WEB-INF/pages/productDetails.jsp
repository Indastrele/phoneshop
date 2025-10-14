<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master>
  <div>
    <a href="${pageContext.request.contextPath}/productList">&larr; Back to product list</a>
    <br>
    <table class="product-information-table">
      <colgroup>
        <col class="base-info-column"/>
        <col class="advanced-info-column"/>
      </colgroup>
      <tr>
        <td>
          <h2>${phone.model}</h2>
          <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
          <p>${phone.description}</p>
          <div class="phone-details-quantity-block">
            <span><b>Price:</b> ${phone.price}$</span> <br>
            <span id="add-to-cart-block">
              <input type="text" value="1">
              <button class="add-to-cart-button"
                      onclick="clickToAddFromDetailsPage(${phone.id}, '${pageContext.request.contextPath}/ajaxCart')">
                Add to cart
              </button>
              <br>
            </span>
          </div>
        </td>
        <td>
          <div>
            <b>Display</b>
            <table class="small-phone-info-block">
              <tags:parameter name="Size"
                              value="${phone.displaySizeInches != null ? phone.displaySizeInches : ''}"
                              dimension="\'\'"/>
              <tags:parameter name="Resolution"
                              value="${phone.displayResolution != null ? phone.displayResolution : ''}"/>
              <tags:parameter name="Technology"
                              value="${phone.displayTechnology != null ? phone.displayTechnology : ''}"/>
              <tags:parameter name="Pixel density"
                              value="${phone.pixelDensity != null ? phone.pixelDensity : ''}"/>
            </table>
          </div>
          <br>
          <div>
            <b>Dimensions & weight</b>
            <table class="small-phone-info-block">
              <tags:parameter name="Length"
                              value="${phone.lengthMm != null ? phone.lengthMm : ''}"
                              dimension="mm"/>
              <tags:parameter name="Width"
                              value="${phone.widthMm != null ? phone.widthMm : ''}"
                              dimension="mm"/>
              <tags:parameter name="Weight"
                              value="${phone.weightGr != null ? phone.weightGr : ''}"
                              dimension="gr"/>
            </table>
          </div>
          <br>
          <div>
            <b>Camera</b>
            <table class="small-phone-info-block">
              <tags:parameter name="Front"
                              value="${phone.frontCameraMegapixels != null ? phone.frontCameraMegapixels : ''}"
                              dimension="megapixels"/>
              <tags:parameter name="Back"
                              value="${phone.backCameraMegapixels != null ? phone.backCameraMegapixels : ''}"
                              dimension="megapixels"/>
            </table>
          </div>
          <br>
          <div>
            <b>Battery</b>
            <table class="small-phone-info-block">
              <tags:parameter name="Talk time"
                              value="${phone.talkTimeHours != null ? phone.talkTimeHours : ''}"
                              dimension="hours"/>
              <tags:parameter name="Stand by time"
                              value="${phone.standByTimeHours != null ? phone.standByTimeHours : ''}"
                              dimension="hours"/>
              <tags:parameter name="Battery capacity"
                              value="${phone.batteryCapacityMah != null ? phone.batteryCapacityMah : ''}"
                              dimension="mAh"/>
            </table>
          </div>
          <br>
          <div>
            <b>Other</b>
            <table class="small-phone-info-block">
              <tr>
                <td>Colors</td>
                <td>
                  <c:if test="${phone.colors.size() > 0}">
                    <c:forEach var="color" items="${phone.colors}" varStatus="loop">
                      ${color.code}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                  </c:if>
                </td>
              </tr>
              <tags:parameter name="Device type" value="${phone.deviceType != null ? phone.deviceType : ''}"/>
              <tags:parameter name="Bluetooth" value="${phone.bluetooth != null ? phone.bluetooth : ''}"/>
            </table>
          </div>
        </td>
      </tr>
    </table>
  </div>
</tags:master>
