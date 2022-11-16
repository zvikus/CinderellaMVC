function fallbackCopyTextToClipboard(text) {
  var textArea = document.createElement("textarea");
  textArea.value = text;

  // Avoid scrolling to bottom
  textArea.style.top = "0";
  textArea.style.left = "0";
  textArea.style.position = "fixed";

  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();

  try {
    var successful = document.execCommand('copy');
    var msg = successful ? 'successful' : 'unsuccessful';
    console.log('Fallback: Copying text command was ' + msg);
  } catch (err) {
    console.error('Fallback: Oops, unable to copy', err);
  }

  document.body.removeChild(textArea);
}
function copyTextToClipboard(text) {
  if (!navigator.clipboard) {
    fallbackCopyTextToClipboard(text);
    return;
  }
  navigator.clipboard.writeText(text).then(function() {
    console.log('Async: Copying to clipboard was successful!');
  }, function(err) {
    console.error('Async: Could not copy text: ', err);
  });
}


$( document ).ready(function() {
    /*$("#filterButton").on("click", function() {
        $('.filterBox').collapse();
    })*/
    $(".link-copy").on("click", function(e){
        copyTextToClipboard(e.currentTarget.dataset.url)
    })

    $(".alert").hide();

    $(".add-to-group-link").on("click", function (event) {

                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");

                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) {
                xhr.setRequestHeader(header, token);
                });

                $().dropdown('dispose');
                $().dropdown('close');

                var form = $(this).parent(),
                    self = this,
                    alert = $(this).closest(".alert-box").find(".alert"),
                    formData =  new FormData(form[0]);
                $("#group" + formData.get("organizerId")).text(formData.get("organizerGroup"));

              $.ajax({
                  type: "POST",
                  url: form[0].action,
                  cache: false,
                  timeout: 60000,
                  success: function (message) {
                       $(self).closest(".dropdown").dropdown('dispose');
                      alert.find("strong").text("Добавлено!");
                      alert.fadeTo(2000, 500).slideUp(500, function() {
                            alert.slideUp(500);
                          });

                  },
                  error: function (message) {
                      $(self).closest(".dropdown").dropdown('dispose');
                      alert.find("strong").text("Добавлено ранее!");
                      alert.fadeTo(2000, 500).slideUp(500, function() {
                            alert.slideUp(500);
                          });
                  }
              });
    });

    $(".submit-form-link").on("click", function (event) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");

                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                  $(document).ajaxSend(function(e, xhr, options) {
                    xhr.setRequestHeader(header, token);
                  });

                  $().dropdown('dispose');
                  $().dropdown('close');

                var form = $(this).parent(),
                    self = this;
                var alert = $(this).closest(".alert-box").find(".alert");

                                   $.ajax({
                                       type: "POST",
                                       url: form[0].action,
                                       cache: false,
                                       timeout: 60000,
                                       success: function (message) {
                                            $(self).closest(".dropdown").dropdown('dispose');
                                           alert.find("strong").text("Добавлено!");
                                           alert.fadeTo(2000, 500).slideUp(500, function() {
                                                 alert.slideUp(500);
                                               });

                                       },
                                       error: function (message) {
                                           $(self).closest(".dropdown").dropdown('dispose');
                                           alert.find("strong").text("Добавлено ранее!");
                                           alert.fadeTo(2000, 500).slideUp(500, function() {
                                                 alert.slideUp(500);
                                               });
                                       }
                                   });
                               });

    $(".auto-submit-form").on("change paste blur", function(event) {
        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");

                        var token = $("meta[name='_csrf']").attr("content");
                        var header = $("meta[name='_csrf_header']").attr("content");
                          $(document).ajaxSend(function(e, xhr, options) {
                            xhr.setRequestHeader(header, token);
                          });

        var form = $(this).parent(),
                            self = this,
                            data = {};
        data[this.name] = $(this).val();

        $.post(form[0].action, data, function(res){

        });
    });
});