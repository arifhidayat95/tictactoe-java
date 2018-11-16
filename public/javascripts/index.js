$(document).ready(function () {
   $("input[type='radio']").on('click',function () {

       const url = $(this).val();

       const data = $(".game-type");
       data.each(function () {
           const type = $(this).data('type');
           $(this).attr('href',url+type);
       });
   });
});