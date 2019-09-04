<!DOCTYPE HTML>
<html lang="en">

<head>
    <title>Login</title>
    <!-- Meta tag Keywords -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8"/>
    <meta name="keywords" content=""
    />
    <script>
        addEventListener("load", function () {
            setTimeout(hideURLbar, 0);
        }, false);

        function hideURLbar() {
            window.scrollTo(0, 1);
        }
    </script>
    <!-- Meta tag Keywords -->
    <!-- css files -->
    <link rel="stylesheet" href="css/style.css" type="text/css" media="all"/>
    <!-- Style-CSS -->
    <link rel="stylesheet" href="css/fontawesome-all.css">
    <!-- Font-Awesome-Icons-CSS -->
    <!-- //css files -->
</head>

<body>
<!-- bg effect -->
<div id="bg">
    <canvas></canvas>
    <canvas></canvas>
    <canvas></canvas>
</div>
<!-- //bg effect -->
<!-- title -->
<h1>用户登录中心</h1>
<!-- //title -->
<!-- content -->
<div class="sub-main-w3">
    <form action="" method="post">
        <h2>Login Now
            <i class="fas fa-level-down-alt"></i>
        </h2>
        <div class="form-style-agile">
            <label>
                <i class="fas fa-user"></i>
                Username
            </label>
            <input placeholder="" name="Name" type="text" required="" value="${userId}">
        </div>
        <div class="form-style-agile">
            <label>
                <i class="fas fa-unlock-alt"></i>
                Password
            </label>
            <input placeholder="" name="Password" type="password" required="">
        </div>
        <!-- checkbox -->
        <div class="wthree-text">
            <ul>
                <li>
                    <label class="anim">
                        <input type="checkbox" class="checkbox" required="">
                        <span>Stay Signed In</span>
                    </label>
                </li>
                <li>
                    <a href="#">Forgot Password?</a>
                </li>
            </ul>
        </div>
        <!-- //checkbox -->
        <input type="submit" value="Log In">
    </form>
</div>
<!-- //content -->

<!-- copyright -->
<div class="footer">
    <p>Copyright &copy; 2019 All rights reserved.</p>
</div>
<!-- //copyright -->

<!-- Jquery -->
<script src="js/jquery-3.3.1.min.js"></script>
<!-- //Jquery -->

<!-- effect js -->
<script src="js/canva_moving_effect.js"></script>
<!-- //effect js -->

</body>

</html>