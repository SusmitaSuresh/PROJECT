#include<stdio.h>
#include<math.h>
#include<string.h>
#include <gtk/gtk.h>
#include <stdlib.h>
#include <ctype.h>

int leftBracketCount=0;
int rightBracketCount=0;
const char *text;
GtkWidget *input;
GtkWidget *output;
GtkWidget *postfix_output; 

#define MAX_SIZE 100

typedef struct 
{
    int top;
    char arr[MAX_SIZE];
} CharStack;

void charPush(CharStack *s, char c) 
{
    s->arr[++s->top] = c;
}
char charPop(CharStack *s) 
{
    return s->arr[s->top--];
}
char charPeek(CharStack *s) 
{
    return s->arr[s->top];
}
int charEmpty(CharStack *s) 
{
    return s->top == -1;
}

typedef struct 
{
    int top;
    int arr[MAX_SIZE];
} IntStack;

void intPush(IntStack *s, int v) 
{
    s->arr[++s->top] = v;
}
int intPop(IntStack *s) 
{
    return s->arr[s->top--];
}

int precedence(char op) 
{
    switch (op) {
        case '+': case '-': return 1;
        case '*': case '/': return 2;
        case '^': return 3;
    }
    return -1;
}

int isOperator(char c) 
{
    return (c=='+' || c=='-' || c=='*' || c=='/' || c=='^');
}

char* infixToPostfix(char* infix)       //returns character pointer
{
    CharStack s;
    s.top = -1;
    char* postfix = malloc(strlen(infix) + 10);
    int j = 0,i;
    for(i = 0; infix[i] != '\0'; i++) 
    {
        char c = infix[i];

        if (isalnum(c)) 
        {
            postfix[j++] = c;
        }
        
        else if (c == '(') 
        {
            charPush(&s, c);
        }
        
        else if (c == ')') 
        {
            while (!charEmpty(&s) && charPeek(&s) != '(')
                postfix[j++] = charPop(&s);
            charPop(&s); 
        }
        
        else if (isOperator(c)) 
        {
            while (!charEmpty(&s) &&
                   precedence(c) <= precedence(charPeek(&s)))
            {
                postfix[j++] = charPop(&s);
            }
            charPush(&s, c);
        }
    }
    while (!charEmpty(&s))
        postfix[j++] = charPop(&s);

    postfix[j] = '\0';
    return postfix;
}

int operate(char op, int a, int b) 
{
    switch (op) 
    {
        case '+': return a + b;
        case '-': return a - b;
        case '*': return a * b;
        case '/':
            if (b == 0) 
            { 
                printf("Division by zero!\n"); 
                exit(1);
            }
            return a / b;
        case '^': {
            int result = 1,i;
            for( i = 0; i < b; i++) result *= a;
            return result;
        }
    }
    return 0;
}

int evaluatePostfix(char* post) 
{
    IntStack s;
    s.top = -1;
    int i;
	for (i = 0; post[i] != '\0'; i++) 
    {
        char c = post[i];
        if (isdigit(c)) 
        {
            intPush(&s, c - '0');
        }
        else if (isOperator(c)) 
        {
            int b = intPop(&s);
            int a = intPop(&s);
            intPush(&s, operate(c,a,b));
        }
    }
    return intPop(&s);
}

static void click(GtkWidget *widget, gpointer user_data)
{
    char last;
    char num;
    const gchar *button_text;       //this text must not be modified so to avoid crash/error, we use const 
    const gchar *current_text;      //const means it is read-only. we can't write b_t[0]='a' for ex.
    gchar *new_text;
    button_text = gtk_button_get_label(GTK_BUTTON(widget));
    if(*button_text=='(')
    {
        leftBracketCount++;
    }  
    current_text = gtk_entry_get_text(GTK_ENTRY(input));
    last = current_text[strlen(current_text)-1];
    if(*button_text=='0' && last == '/')        //removes division by 0 case
    {
        gtk_label_set_text(GTK_LABEL(output), "DIVISION BY 0 INVALID, PLEASE REMOVE");
    }
    if(strlen(current_text) == 0 || !isdigit(last) && last!=')')      //numberers can't be added right after number or )
    {
        new_text = g_strconcat(current_text,button_text,NULL);
        gtk_entry_set_text(GTK_ENTRY(input),new_text);
        g_free(new_text);
    }
}

static void clickf(GtkWidget *widget, gpointer user_data)
{
    const gchar *current_text;
    current_text = gtk_entry_get_text(GTK_ENTRY(input));
    if(strlen(current_text)>0)
    {
        char last;
        last = current_text[strlen(current_text)-1];
        if(last==')' || last>47 && last<58)        //functions cant be added after functions, except )
        {
            const gchar *button_text;
            button_text = gtk_button_get_label(GTK_BUTTON(widget)); 
            if(*button_text==')')
            {
                rightBracketCount++;
            } 
            gchar *new_text;
            new_text = g_strconcat(current_text,button_text,NULL);
            gtk_entry_set_text(GTK_ENTRY(input),new_text);
        }
    }
}

static void clear(GtkWidget *widget)
{
    gtk_entry_set_text(GTK_ENTRY(input),"");
    gtk_label_set_text(GTK_LABEL(output), "");
    gtk_label_set_text(GTK_LABEL(postfix_output), "");
    leftBracketCount=0;         //resets bracket counts
    rightBracketCount=0;
}

static void delete(GtkWidget *widget)
{
    if(strlen(gtk_entry_get_text(GTK_ENTRY(input)))>0)
    {
        const gchar *text;
        gchar *newtext;
        text = gtk_entry_get_text(GTK_ENTRY(input));
        char last;
        last = text[strlen(text)-1];
        if(last==')')           //makes sure bracket counts are right
        {
            rightBracketCount--;
        }
        else if(last=='(')
        {
            leftBracketCount--;
        }
        newtext = g_strndup(text, strlen(text)-1);      //duplicates n elements
        gtk_entry_set_text(GTK_ENTRY(input),newtext);
        g_free(newtext);
    }
}

void equal(GtkWidget *widget)
{
    const gchar *text;
    text = gtk_entry_get_text(GTK_ENTRY(input));
    if (text == NULL || strlen(text) == 0)
    {
        return;
    }
    char last;
    last = text[strlen(text)-1];
    if(leftBracketCount>=rightBracketCount)     //removes segmentation fault due to extra right brackets
    {
        if((last>47 && last<58 || last==')'))    //if last digit is a number or ), then it shld be displayed
        { 
            gchar *texts;
            texts = g_strdup(text);
            char *postfix = infixToPostfix(texts);
            gtk_label_set_text(GTK_LABEL(postfix_output), postfix);
            int result = evaluatePostfix(postfix);
            free(postfix);
            g_free(texts);
            char result_char[50];
            sprintf(result_char, "%d", result);
            gtk_label_set_text(GTK_LABEL(output), result_char);
        }
        else
        {
            gtk_label_set_text(GTK_LABEL(output), "error");
        }
    }
    else
    {
        gtk_label_set_text(GTK_LABEL(output), "remove extra bracket(s)");
    }
}

void make_window()
{
    GtkWidget *window;
    GtkWidget *box;
    GtkWidget *grid;
    GtkWidget *b1;
    GtkWidget *b2;
    GtkWidget *b3;
    GtkWidget *b4;
    GtkWidget *b5;
    GtkWidget *b6;
    GtkWidget *b7;
    GtkWidget *b8;
    GtkWidget *b9;
    GtkWidget *b0;
    GtkWidget *bclear;
    GtkWidget *bdelete;
    GtkWidget *brightbracket;
    GtkWidget *bdiv;
    GtkWidget *bmult;
    GtkWidget *bsub;
    GtkWidget *badd;
    GtkWidget *bequal;
    GtkWidget *bleftbracket;
    GtkWidget *bexp;

    {window= gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(window), 300, 500);
    gtk_window_set_title(GTK_WINDOW(window), "single digit calculator");
    g_signal_connect(G_OBJECT(window), "destroy", G_CALLBACK(gtk_main_quit), NULL);}

    box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL,5);

    {grid = gtk_grid_new();
    gtk_widget_set_hexpand(grid, TRUE);
    gtk_widget_set_vexpand(grid, TRUE);
    gtk_box_pack_start(GTK_BOX(box), grid, TRUE, TRUE, 10);}

    output = gtk_label_new("output");
    postfix_output = gtk_label_new("postfix shows up here");

    input = gtk_entry_new();
    gtk_editable_set_editable(GTK_EDITABLE(input), FALSE);

    {b0 = gtk_button_new_with_label("0");  
    b1 = gtk_button_new_with_label("1");
    b2 = gtk_button_new_with_label("2");
    b3 = gtk_button_new_with_label("3");
    b4 = gtk_button_new_with_label("4");
    b5 = gtk_button_new_with_label("5");
    b6 = gtk_button_new_with_label("6");
    b7 = gtk_button_new_with_label("7");
    b8 = gtk_button_new_with_label("8");
    b9 = gtk_button_new_with_label("9");
    bclear = gtk_button_new_with_label("cl");  
    bdelete = gtk_button_new_with_label("del");
    brightbracket = gtk_button_new_with_label(")");
    bdiv = gtk_button_new_with_label("/");
    bmult = gtk_button_new_with_label("*");
    bsub = gtk_button_new_with_label("-");
    badd = gtk_button_new_with_label("+");
    bequal = gtk_button_new_with_label("=");
    bleftbracket = gtk_button_new_with_label("(");
    bexp = gtk_button_new_with_label("^");}

    {gtk_grid_attach(GTK_GRID(grid), input, 0, -3, 4, 1);       //attaches to grid, -ve values of index allowed
    gtk_grid_attach(GTK_GRID(grid), postfix_output, 0, -2, 4, 1);
    gtk_grid_attach(GTK_GRID(grid), output, 0, -1, 4, 1);
    gtk_grid_attach(GTK_GRID(grid), bclear, 0, 0, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bdelete, 1, 0, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bleftbracket, 2, 0, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), brightbracket, 3, 0, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b1, 0, 1, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b2, 1, 1, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b3, 2, 1, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bmult, 3, 1, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b4, 0, 2, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b5, 1, 2, 1, 1); 
    gtk_grid_attach(GTK_GRID(grid), b6, 2, 2, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bsub, 3, 2, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b7, 0, 3, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b8, 1, 3, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b9, 2, 3, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), badd, 3, 3, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bexp, 0, 4, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), b0, 1, 4, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bdiv, 2, 4, 1, 1);
    gtk_grid_attach(GTK_GRID(grid), bequal, 3, 4, 1, 1);}


    {gtk_widget_set_hexpand(b1, TRUE);      //sets expansion for vertical and horizontal to true
    gtk_widget_set_vexpand(b1, TRUE);
    gtk_widget_set_hexpand(b2, TRUE);
    gtk_widget_set_vexpand(b2, TRUE);
    gtk_widget_set_hexpand(b3, TRUE);
    gtk_widget_set_vexpand(b3, TRUE);
    gtk_widget_set_hexpand(b4, TRUE);
    gtk_widget_set_vexpand(b4, TRUE);
    gtk_widget_set_hexpand(b5, TRUE);
    gtk_widget_set_vexpand(b5, TRUE);
    gtk_widget_set_hexpand(b6, TRUE);
    gtk_widget_set_vexpand(b6, TRUE);
    gtk_widget_set_hexpand(b7, TRUE);
    gtk_widget_set_vexpand(b7, TRUE);
    gtk_widget_set_hexpand(b8, TRUE);
    gtk_widget_set_vexpand(b8, TRUE);
    gtk_widget_set_hexpand(b9, TRUE);
    gtk_widget_set_vexpand(b9, TRUE);
    gtk_widget_set_hexpand(b0, TRUE);
    gtk_widget_set_vexpand(b0, TRUE);
    gtk_widget_set_hexpand(bclear, TRUE);
    gtk_widget_set_vexpand(bclear, TRUE);
    gtk_widget_set_hexpand(bdelete, TRUE);
    gtk_widget_set_vexpand(bdelete, TRUE);
    gtk_widget_set_hexpand(bleftbracket, TRUE);
    gtk_widget_set_vexpand(bleftbracket, TRUE);
    gtk_widget_set_hexpand(bdiv, TRUE);
    gtk_widget_set_vexpand(bdiv, TRUE);
    gtk_widget_set_hexpand(bmult, TRUE);
    gtk_widget_set_vexpand(bmult, TRUE);
    gtk_widget_set_hexpand(bsub, TRUE);
    gtk_widget_set_vexpand(bsub, TRUE);
    gtk_widget_set_hexpand(badd, TRUE);
    gtk_widget_set_vexpand(badd, TRUE);
    gtk_widget_set_hexpand(bequal, TRUE);
    gtk_widget_set_vexpand(bequal, TRUE);
    gtk_widget_set_hexpand(brightbracket, TRUE);
    gtk_widget_set_vexpand(brightbracket, TRUE);
    gtk_widget_set_hexpand(bexp, TRUE);
    gtk_widget_set_vexpand(bexp, TRUE);
    gtk_widget_set_hexpand(output, TRUE);
    gtk_widget_set_vexpand(output, TRUE);
    gtk_widget_set_hexpand(input, TRUE);
    gtk_widget_set_vexpand(input, TRUE);}
    

    {g_signal_connect(G_OBJECT(b1), "clicked", G_CALLBACK(click), NULL);    //calls function when button clicked via callback
    g_signal_connect(G_OBJECT(b2), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b3), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b4), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b5), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b6), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b7), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b8), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b9), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(b0), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(bleftbracket), "clicked", G_CALLBACK(click), NULL);
    g_signal_connect(G_OBJECT(bdiv), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(badd), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(bsub), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(bmult), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(brightbracket), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(bexp), "clicked", G_CALLBACK(clickf), NULL);
    g_signal_connect(G_OBJECT(bclear), "clicked", G_CALLBACK(clear), NULL);
    g_signal_connect(G_OBJECT(bdelete), "clicked", G_CALLBACK(delete), NULL);
    g_signal_connect(G_OBJECT(bequal), "clicked", G_CALLBACK(equal), NULL);}

    gtk_container_add(GTK_CONTAINER(window), box);
    gtk_widget_show_all(window);
}

int main (int argc, char *argv[])
{
    gtk_init(&argc, &argv);
    make_window();
    gtk_main();
    return 0;
}
