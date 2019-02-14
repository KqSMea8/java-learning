package me.melon.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author melon.zhao
 * @since 2019/2/14
 */
public class RpcTaste {

    public static void export(final Object service, int port) throws IOException {
        if (service == null) {
            throw new IllegalArgumentException("service is null");
        }
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port: " + port);
        }
        System.out.println("export service " + service.getClass().getName() + " on port: " + port);
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try {
                final Socket accept = serverSocket.accept();
                new Thread(() -> {
                    ObjectInputStream ois = null;
                    try {
                        ois = new ObjectInputStream(accept.getInputStream());
                        String methodName = ois.readUTF();
                        Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();
                        Object[] arguments = (Object[]) ois.readObject();
                        ObjectOutputStream out = new ObjectOutputStream(accept.getOutputStream());
                        try {
                            Method method = service.getClass().getMethod(methodName, paramterTypes);
                            Object result = method.invoke(service, arguments);
                            out.writeObject(result);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            out.close();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    } finally {
                        try {
                            ois.close();
                            accept.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T refer(final Class<T> interfaceClz, final String host, final int port) {
        if (interfaceClz == null) {
            throw new IllegalArgumentException("interface is null");
        }
        if (!interfaceClz.isInterface()) {
            throw new IllegalArgumentException("the " + interfaceClz.getName() + " must be interface class~");
        }
        if (host == null || host.length() == 0) {
            throw new IllegalArgumentException("host is null");
        }
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port: " + port);
        }

        System.out.println("get remote service " + interfaceClz.getName() + " from server:" + host + " port:" + port);
        return (T) Proxy.newProxyInstance(interfaceClz.getClassLoader(), new Class[]{interfaceClz},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Socket socket = new Socket(host, port);
                    try {
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        try {
                            outputStream.writeUTF(method.getName());
                            outputStream.writeObject(method.getParameterTypes());
                            outputStream.writeObject(args);

                            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            try {
                                Object result = inputStream.readObject();
                                if (result instanceof Throwable) {
                                    throw (Throwable) result;
                                }
                                return result;
                            }finally {
                                inputStream.close();
                            }
                        } finally {
                            outputStream.close();
                        }
                    }finally {
                        socket.close();
                    }
                }
            });
    }


}









