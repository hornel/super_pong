from Tkinter import *
from tkColorChooser import askcolor
from random import randint
import os


class Pong(Tk):

    # ----------------------------------------------------------------------------- #
    #                                                                               #
    #                             GAME INITIALIZATION                               #
    #                                                                               #
    # ----------------------------------------------------------------------------- #

    def __init__(self, parent):

        self.parent = parent
        Tk.__init__(self, parent)

        self.x_traj = 9
        self.y_traj = 9
        self.y_bend_factor = 3

        self.paddle_size = 100

        self.fgcolor = "white"
        self.bgcolor = "black"
        self.level = 2

        self.ball_start = randint(0, 500)

        self.player = 2

        self.score1 = 0
        self.score2 = 0

        self.width = 950
        self.height = 620

        self.paused = False

        self.title("Pong")
        self.configure(bg="#ededed")
        self.resizable(False, False)
        self.setup_frames()

        if os.name == "posix":  # workaround for Tkinter bug on Mac where window is not shown
            os.system("""/usr/bin/osascript -e 'tell app "Finder" to set frontmost of process "Python" to true'""")

    def setup_frames(self):

        self.setup_menubar()

        self.root1 = Frame(self, bg="#ededed")
        self.setup_canvas()

        self.root2 = Frame(self, bg="#ededed")
        self.setup_controls()

        self.root1.grid(row=0, column=0)
        self.root2.grid(row=1, column=0)

    # ----------------------------------------------------------------------------- #
    #                                                                               #
    #                                 GAME SETUP                                    #
    #                                                                               #
    # ----------------------------------------------------------------------------- #

    # --------------------------------- CANVAS SETUP --------------------------------

    def setup_canvas(self):

        self.canvas = Canvas(self.root1, bg="black", width=self.width, height=self.height, highlightbackground="#ededed")

        self.score1_l = self.canvas.create_text(300, 50, font="Menlo 50", text=self.score1, fill="white")
        self.score2_l = self.canvas.create_text(650, 50, font="Menlo 50", text=self.score2, fill="white")

        self.line = self.canvas.create_line(self.width/2, self.height, self.width/2, 0, fill="white", width=4, dash=20)

        self.ball = self.canvas.create_oval(10 + self.ball_start, 10 + self.ball_start, 30 + self.ball_start, 30 + self.ball_start, fill="white")
        self.setup_paddles()

        self.canvas.grid(row=0, column=0)

        self.move_ball()

    # --------------------------------- PADDLE SETUP --------------------------------

    def setup_paddles(self):

        self.paddle1 = self.canvas.create_rectangle(13, 250, 33, 250 + self.paddle_size, fill="white")
        self.paddle2 = self.canvas.create_rectangle(self.width - 10, self.height / 2 - (self.paddle_size / 2), self.width - 30, self.height / 2 + (self.paddle_size / 2), fill="white")

        self.canvas.bind_all("<Motion>", self.move_paddles)

    def move_paddles(self, event):  # move paddles with the mouse

        paddle1_coords = self.canvas.coords(self.paddle1)
        paddle2_coords = self.canvas.coords(self.paddle2)

        if not self.paused:
            if self.player == 1:
                paddle1_coords[1] = event.y + self.paddle_size / 2
                paddle1_coords[3] = event.y - self.paddle_size / 2
                self.canvas.coords(self.paddle1, *paddle1_coords)

            if self.player == 2:
                paddle2_coords[1] = event.y + self.paddle_size / 2
                paddle2_coords[3] = event.y - self.paddle_size / 2
                self.canvas.coords(self.paddle2, *paddle2_coords)

    # -------------------------------- BALL SETUP ------------------------------------

    def move_ball(self):  # main loop for moving the ball

        if not self.paused:
            self.canvas.move(self.ball, self.x_traj, self.y_traj)
            self.check_collisions()
            self.focus()
            self.after(1, self.move_ball)

    def check_collisions(self):  # checks if the ball hits any boundaries and modifies the trajectory accordingly

        ball_coords = self.canvas.coords(self.ball)
        paddle1_coords = self.canvas.coords(self.paddle1)
        paddle2_coords = self.canvas.coords(self.paddle2)

        if (ball_coords[0] <= paddle1_coords[2]) and (ball_coords[1] > paddle1_coords[1]) and (ball_coords[3] < paddle1_coords[3]):  # if the ball hits paddle 1
            self.check_paddle_collisions(1, self.paddle1)

        elif (ball_coords[2] >= paddle2_coords[0]) and (ball_coords[1] > paddle2_coords[1]) and (ball_coords[3] < paddle2_coords[3]):  # if the ball hits paddle 2
            self.check_paddle_collisions(2, self.paddle2)

        elif ball_coords[1] <= 0 or ball_coords[3] >= self.height:  # if the ball hits the top or bottom of the canvas
            self.y_traj = -self.y_traj

        elif ball_coords[0] <= -35:  # if player 1 misses the ball
            self.score2 += 1
            self.canvas.itemconfig(self.score2_l, text=str(self.score2))
            self.after(1000)
            self.y_traj = abs(self.x_traj)
            self.x_traj = -self.x_traj
            self.canvas.coords(self.ball, (30, 1, 50, 21))
            self.player = 2

        elif ball_coords[2] >= self.width + 35:  # if player 2 misses the ball
            self.score1 += 1
            self.canvas.itemconfig(self.score1_l, text=str(self.score1))
            self.after(1000)
            self.y_traj = -(abs(self.x_traj))
            self.x_traj = -self.x_traj
            self.canvas.coords(self.ball, (self.width - 50, 1, self.width - 30, 21))
            self.player = 1

        if self.score1 == 10 or self.score2 == 10:
            self.pause_game()
            won = self.canvas.create_text(self.width / 2, self.height / 2, font="Menlo 70", text="PLAYER %d WINS!!!" % self.player, fill=self.fgcolor)

    def check_paddle_collisions(self, player, paddle):  # modify ball trajectory according to the place where it hits the paddle

        ball_coords = self.canvas.coords(self.ball)
        paddle_coords = self.canvas.coords(paddle)

        paddle_center = paddle_coords[1] + ((paddle_coords[3] - paddle_coords[1]) / 2)
        ball_hit = ball_coords[1] + ((ball_coords[3] - ball_coords[1]) / 2)

        dist = abs(ball_hit - paddle_center)

        self.x_traj = -self.x_traj
        self.y_traj = (dist / self.y_bend_factor) * (self.y_traj / abs(self.y_traj))
        if self.y_traj == 0:
            self.y_traj += 0.01

        if player == 1:
            self.after(10, self.unstick_ball(1))
            self.player = 2
        else:
            self.after(10, self.unstick_ball(2))
            self.player = 1

    def unstick_ball(self, paddle):  # workaround for bug where ball sticks to paddle

        paddle1_coords = self.canvas.coords(self.paddle1)
        paddle2_coords = self.canvas.coords(self.paddle2)
        ball_coords = self.canvas.coords(self.ball)

        def ball_stuck():

            if paddle == 1:
                return ball_coords[0] <= paddle1_coords[2]
            else:
                return ball_coords[2] >= paddle2_coords[0]

        if ball_stuck():
            if paddle == 1:
                ball_coords[0] = paddle1_coords[2]
                ball_coords[2] = ball_coords[0] + 20
            elif paddle == 2:
                ball_coords[2] = paddle2_coords[0]
                ball_coords[0] = ball_coords[2] - 20
            self.canvas.coords(self.ball, *ball_coords)

    # ----------------------------------------------------------------------------- #
    #                                                                               #
    #                                 GUI SETUP                                     #
    #                                                                               #
    # ----------------------------------------------------------------------------- #

    # -------------------------------- MENU BAR SETUP -----------------------------

    def setup_menubar(self):  # configure commands in menu bar

        menubar = Menu(self)
        edit_menu = Menu(menubar, tearoff=0)
        edit_menu.add_command(label="Preferences...", command=self.show_prefs)
        menubar.add_cascade(label="Edit", menu=edit_menu)

        self.configure(menu=menubar)

    # ----------------------------- PREFERENCES SETUP -----------------------------

    def show_prefs(self):  # display preferences window

        self.current_prefs = [self.fgcolor, self.bgcolor, self.level, self.x_traj, self.y_bend_factor]

        self.prefs_win = Toplevel(self, bg="#ededed")
        self.prefs_win.wm_title("Preferences")
        self.prefs_win.resizable(False, False)
        self.prefs_win.attributes("-topmost", 1)

        self.bgcolor_l = Label(self.prefs_win, text="This is the current background color", fg=self.bgcolor, bg="#ededed")
        self.fgcolor_l = Label(self.prefs_win, text="This is the current foreground color", fg=self.fgcolor, bg="#ededed")

        self.bgcolor_b = Button(self.prefs_win, text="Change Background Color...", highlightbackground="#ededed", command=lambda: self.change_colors("bg"))
        self.fgcolor_b = Button(self.prefs_win, text="Change Foreground Color...", highlightbackground="#ededed", command=lambda: self.change_colors("fg"))

        self.difficulty = IntVar()
        self.difficulty.set(self.level)

        self.modes = [("Easy", 1), ("Medium", 2), ("Hard", 3), ("Expert", 4), ("Custom...", 5)]

        self.bgcolor_l.grid(row=0, column=0, padx=5, pady=(5, 0))
        self.bgcolor_b.grid(row=1, column=0, padx=5, pady=(0, 5))
        self.fgcolor_l.grid(row=2, column=0, padx=5, pady=(5, 0))
        self.fgcolor_b.grid(row=3, column=0, padx=5, pady=(0, 10))

        Label(self.prefs_win, text="Difficulty Level:", bg="#ededed").grid(row=4, column=0, sticky=W, padx=5)

        counter = 5

        for text, value in self.modes:
            m = Radiobutton(self.prefs_win, text=text, value=value, variable=self.difficulty, bg="#ededed", command=self.change_difficulty)
            m.grid(row=counter, column=0, sticky=W, padx=10)
            counter += 1

        Button(self.prefs_win, text="OK", highlightbackground="#ededed", default=ACTIVE, command=self.prefs_win.destroy).grid(row=10, column=0, sticky=E, padx=5, pady=5)
        Button(self.prefs_win, text="Cancel", highlightbackground="#ededed", command=self.cancel_prefs_change).grid(row=10, column=0, sticky=E, padx=55, pady=5)

    def cancel_prefs_change(self):

        self.change_colors("fg", user=False, color=self.current_prefs[0])
        self.change_colors("bg", user=False, color=self.current_prefs[1])
        self.change_difficulty(user=False, change=self.current_prefs[2])
        self.x_traj = self.current_prefs[3]
        self.y_bend_factor = self.current_prefs[4]

        self.prefs_win.destroy()

    def change_colors(self, where, user=True, color=None):  # change "theme" of game

        if user:
            choose_color = askcolor()[1]
        else:
            choose_color = color

        if where == "bg":
            self.bgcolor = choose_color
            self.canvas.configure(bg=choose_color)
            self.bgcolor_l.configure(fg=self.bgcolor)
        elif where == "fg":
            self.fgcolor = choose_color
            self.canvas.itemconfig(self.ball, fill=choose_color)
            self.canvas.itemconfig(self.paddle1, fill=choose_color)
            self.canvas.itemconfig(self.paddle2, fill=choose_color)
            self.canvas.itemconfig(self.score1_l, fill=choose_color)
            self.canvas.itemconfig(self.score2_l, fill=choose_color)
            self.canvas.itemconfig(self.line, fill=choose_color)
            self.fgcolor_l.configure(fg=self.fgcolor)

    def change_difficulty(self, user=True, change=None):  # change game difficulty

        if user:
            mode = self.difficulty.get()
        else:
            mode = change

        self.level = mode
        self.difficulty.set(self.level)
        paddle1_coords = self.canvas.coords(self.paddle1)
        paddle2_coords = self.canvas.coords(self.paddle2)

        if mode == 1:
            self.speed_s.set(5)
            self.paddle_size = 100

        elif mode == 2:
            self.speed_s.set(9)
            self.paddle_size = 100

        elif mode == 3:
            self.speed_s.set(15)
            self.paddle_size = 100

        elif mode == 4:
            self.speed_s.set(15)
            self.paddle_size = 50

        elif mode == 5:
            self.show_difficulty_manager()

        if not mode == 5:
            self.change_ball_speed(self.speed_s)

        paddle1_coords[3] = paddle1_coords[1] + self.paddle_size
        paddle2_coords[3] = paddle2_coords[1] + self.paddle_size
        self.canvas.coords(self.paddle1, *paddle1_coords)
        self.canvas.coords(self.paddle2, *paddle2_coords)

    def show_difficulty_manager(self):  # window with advanced difficulty settings

        self.dm_win = Toplevel(self.prefs_win, bg="#ededed")
        self.dm_win.wm_title("Custom Difficulty")
        self.dm_win.resizable(False, False)
        self.dm_win.attributes("-topmost", 1)

        self.paddle_size_s = Scale(self.dm_win, bg="#ededed", from_=25, to=100, length=400, orient="horizontal", command=lambda e: self.get_advanced_vars("paddlesize"))
        self.advanced_speed_s = Scale(self.dm_win, bg="#ededed", from_=1, to=30, resolution=0.1, length=400, orient="horizontal", command=lambda e: self.change_ball_speed(self.advanced_speed_s))
        self.y_bend_factor_s = Scale(self.dm_win, bg="#ededed", from_=1, to=50, resolution=1, length=400, orient="horizontal", command=lambda e: self.get_advanced_vars("ybend"))
        self.paddle_size_s.set(self.paddle_size)
        self.advanced_speed_s.set(self.speed_s.get())
        self.y_bend_factor_s.set(self.y_bend_factor)

        Label(self.dm_win, text="Paddle size:", bg="#ededed").grid(row=0, column=0, padx=5, pady=5)
        self.paddle_size_s.grid(row=1, column=0, padx=5, pady=5)

        Label(self.dm_win, text="Ball speed:", bg="#ededed").grid(row=2, column=0, padx=5, pady=(10, 5))
        self.advanced_speed_s.grid(row=3, column=0, padx=5, pady=5)

        Label(self.dm_win, text="Reflection factor (higher value = less reflection)", bg="#ededed").grid(row=4, column=0, padx=5, pady=(10, 5))
        self.y_bend_factor_s.grid(row=5, column=0, padx=5, pady=5)

    def get_advanced_vars(self, var):  # function for assigning result of advanced difficulty settings to variables

        if var == "paddlesize":
            self.paddle_size = self.paddle_size_s.get()
        elif var == "ybend":
            self.y_bend_factor = self.y_bend_factor_s.get()

    # ----------------------------- GAME CONTROLS SETUP -----------------------------

    def setup_controls(self):  # set up basic game controls/make them accessible: pause, new game, quit, ball speed

        self.stop = Button(self.root2, text="Quit", highlightbackground="#ededed", command=self.quit)
        self.pause = Button(self.root2, text="Pause [space]", highlightbackground="#ededed", command=self.pause_game)
        self.restart = Button(self.root2, text="New Game", highlightbackground="#ededed", command=self.restart_game)

        self.speed_l = Label(self.root2, text="Ball speed:", bg="#ededed")
        self.speed_s = Scale(self.root2, from_=1, to=15, bg="#ededed", resolution=0.1, orient="horizontal", length=200, command=lambda e: self.change_ball_speed(self.speed_s))
        self.speed_s.set(9)

        self.stop.grid(row=0, column=0, padx=5, pady=5)
        self.pause.grid(row=0, column=1, padx=5, pady=5)
        self.restart.grid(row=0, column=2, padx=5, pady=5)
        self.speed_l.grid(row=0, column=3, padx=10, pady=5)
        self.speed_s.grid(row=0, column=4, pady=5)

        self.bind("<space>", lambda e: self.pause_game())

    def change_ball_speed(self, where):  # function for changing the x- and y-trajectory of the ball

        new_speed = where.get()
        if self.x_traj > 0:
            self.x_traj = new_speed
        else:
            self.x_traj = -new_speed

        self.y_bend_factor = 18.867 * (new_speed**-0.922)

        # Table:
        #
        # x_traj | y_bend_factor
        # ----------------------
        #    1   |      20
        #    2   |      10
        #    3   |       7
        #    4   |       5
        #    5   |       4             => Trendline in Excel -> equation: y = 18.867 * x^-0.922
        #    6   |       3
        #    7   |       3
        #    8   |       3
        #    9   |       3

    def pause_game(self):

        if not self.paused:
            self.paused = True

        elif self.paused:
            self.paused = False
            self.after(1000)
            self.move_ball()

    def restart_game(self):

        self.destroy()
        self.__init__(None)  # new pong game


if __name__ == "__main__":
    app = Pong(None)
    app.mainloop()