#class Hello:
def displayHello(name,a,b):
        stacktrace = 'Python stacktrace:'
        try:
            print("hello world!  "+name)
            print("Sum is: ",a+b)
            stacktrace = stacktrace + " Hello..."+name+" Sum is: " + str(a+b)
        except (BaseException, Exception, ImportError, TypeError) as e:
            stacktrace = stacktrace + ' \n \n Custom Exception:\n' + str(e)
            print('Exception Handled')
        return stacktrace        