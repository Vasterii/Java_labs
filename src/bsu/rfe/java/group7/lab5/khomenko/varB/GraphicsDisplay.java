package bsu.rfe.java.group7.lab5.khomenko.varB;

import java.awt.*;
import java.awt.geom.*;
import java.text.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JPanel;

@SuppressWarnings({ "serial" })
public class GraphicsDisplay extends JPanel {

    class GraphPoint {
        double xd, yd;
        int x, y;
        int n;
    }

    class Zone {
        double MAXY, MINY, MAXX, MINX;
        boolean use;
    }

    private Zone zone = new Zone();

    // Список координат точек для построения графика
    private double[][] graphicsData;
    private int[][] graphicsDataI;

    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true, showMarkers = true, showGrid = true, showLevels = false;

    private final boolean PPP = false;
    private boolean zoom = false;
    private boolean selMode = false;
    private boolean dragMode = false;

    // Границы диапазона пространства, подлежащего отображению
    private double minX, maxX, minY, maxY;

    // Используемые масштаб отображения
    private double scale, scaleX, scaleY;

    private final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    // Различные стили черчения линий
    private final BasicStroke graphicsStroke;
    private final BasicStroke gridStroke;
    private final BasicStroke hatchStroke;
    private final BasicStroke axisStroke;
    private final BasicStroke selStroke;
    private final BasicStroke markerStroke;
    private final BasicStroke levelStroke;

    // Различные шрифты отображения надписей
    private final Font axisFont;
    private final Font captionFont;
    private final Font levelsFont;

    private int mausePX = 0, mausePY = 0;
    private GraphPoint SMP;
    double xmax;
    private final Rectangle2D.Double rect;
    private final Stack<Zone> zoneStack = new Stack<Zone>();


    public GraphicsDisplay() {

        setBackground(Color.WHITE);

        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {5,5,5,5,5,5,20,5,10,5,10,5}, 0.0f);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
        hatchStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
        axisStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        selStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, new float[] { 8, 8 }, 0.0f);
        levelStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, new float [] {3, 2}, 0.0f);


        axisFont = new Font("Serif", Font.BOLD, 20);
        captionFont = new Font("Serif", Font.BOLD, 10);
        levelsFont = new Font("Serif", Font.PLAIN, 20);


        MouseMotionHandler mouseMotionHandler = new MouseMotionHandler();
        addMouseMotionListener(mouseMotionHandler);
        addMouseListener(mouseMotionHandler);
        rect = new Rectangle2D.Double();
        zone.use = false;
    }

    // Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(double[][] graphicsData) {
        this.graphicsData = graphicsData;
        graphicsDataI = new int[graphicsData.length][2];
        repaint();
    }

    // Методы-модификаторы для изменения параметров отображения графика
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowLevels(boolean showLevels) {
        this.showLevels = showLevels;
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }


    public int getDataLenght() {
        return graphicsData.length;
    }

    public double getValue(int i, int j) {
        return graphicsData[i][j];
    }


    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {

        super.paintComponent(g); // Заливка области цветом заднего фона
        if (graphicsData == null || graphicsData.length == 0)
            return;

        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        if (zone.use) {
            minX = zone.MINX;
            maxX = zone.MAXX;
        }

        for (int i = 1; i < graphicsData.length; i++) {

            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
                xmax = graphicsData[i][1];
            }
        }

        if (zone.use) {
            minY = zone.MINY;
            maxY = zone.MAXY;
        }
        scaleX = getSize().getWidth() / (maxX - minX);
        scaleY = getSize().getHeight() / (maxY - minY);



        scale = Math.min(scaleX, scaleY);
        if(!zoom){
            if (scale == scaleX) {
                double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
                maxY += yIncrement;
                minY -= yIncrement;
            }

            if (scale == scaleY) {
                double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
                maxX += xIncrement;
                minX -= xIncrement;
            }
        }
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showGrid)
            paintGrid(canvas);
        if (showAxis)
            paintAxis(canvas);
        if (showLevels)
            paintLevelLines(canvas);
        paintGraphics(canvas);
        if (showMarkers)
            paintMarkers(canvas);
        if (SMP != null)
            paintHint(canvas);
        if (selMode) {
            canvas.setColor(Color.BLACK);
            canvas.setStroke(selStroke);
            canvas.draw(rect);
        }

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    protected void paintHint(Graphics2D canvas) {

        Color oldColor = canvas.getColor();
        canvas.setColor(Color.MAGENTA);
        StringBuffer label = new StringBuffer();
        label.append("x = ");
        label.append(formatter.format((SMP.xd)));
        label.append(", y = ");
        label.append(formatter.format((SMP.yd)));
        FontRenderContext context = canvas.getFontRenderContext();
        Rectangle2D bounds = captionFont.getStringBounds(label.toString(),context);


        int dy = -10;
        int dx = +7;
        if (SMP.y < bounds.getHeight())
            dy = +13;
        if (getWidth() < bounds.getWidth() + SMP.x + 20)
            dx = -(int) bounds.getWidth() - 15;
        canvas.drawString (label.toString(), SMP.x + dx, SMP.y + dy);


        canvas.setColor(oldColor);
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {

        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        GeneralPath graphics = new GeneralPath();

        for (int i = 0; i < graphicsData.length; i++) {

            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);

            graphicsDataI[i][0] = (int) point.getX();
            graphicsDataI[i][1] = (int) point.getY();

            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            }
            else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }

        canvas.draw(graphics);
    }

    protected void paintHatch(Graphics2D canvas, double x1, double y1, double step) {

        Color oldColor = canvas.getColor();
        Stroke oldStroke = canvas.getStroke();
        canvas.setColor(Color.GRAY);
        canvas.setStroke(hatchStroke);
        GeneralPath graphics = new GeneralPath();

        int uu = 0;
        int y = (int) xyToPoint(0, 0).getY();
        int x;
        int d = 0;
        for (double i = x1 + step; i < maxX; i += step) {
            uu++;
            if (uu == 5) {
                uu = -5;
                d = 5;
            }
            else
                d = 0;
            x = (int) xyToPoint(i, 0).getX();

            if (x > getWidth() - 22)
                break;


            graphics.moveTo(x, y - 5 - d);
            graphics.lineTo(x, y + 5 + d);
        }

        uu = 0;
        for (double i = x1 - step; i > minX; i -= step) {
            uu++;
            if (uu == 5) {
                uu = -5;
                d = 5;
            }
            else
                d = 0;

            x = (int) xyToPoint(i, 0).getX();
            graphics.moveTo(x, y - 5 - d);
            graphics.lineTo(x, y + 5 + d);
        }
        x = (int) xyToPoint(0, 0).getX();
        uu = 0;
        for (double i = y1 + step; i < maxY; i += step) {
            uu++;
            if (uu == 5) {
                uu = -5;
                d = 5;
            }
            else
                d = 0;

            y = (int) xyToPoint(0, i).getY();
            if (y < 20)
                break;
            graphics.moveTo(x - 5 - d, y);
            graphics.lineTo(x + 5 + d, y);
        }

        uu = 0;
        for (double i = y1 - step; i > minY; i -= step) {
            uu++;
            if (uu == 5) {
                uu = -5;
                d = 5;
            }
            else
                d = 0;

            y = (int) xyToPoint(0, i).getY();
            graphics.moveTo(x - 5 - d, y);
            graphics.lineTo(x + 5 + d, y);
        }
        canvas.draw(graphics);
        canvas.setStroke(oldStroke);
        canvas.setColor(oldColor);
    }

    // Отображение горизонтальных линий, отмечающие уровни на графике
    protected void paintLevelLines(Graphics2D canvas) {

        double lvl1, lvl2, lvl3;
        lvl1 = minY + 0.9 * (maxY - minY);
        lvl2 = minY + 0.5 * (maxY - minY);
        lvl3 = minY + 0.1 * (maxY - minY);

        canvas.setStroke(levelStroke);
        canvas.setColor(Color.GREEN);
        canvas.setFont(levelsFont);
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl1), xyToPoint(maxX, lvl1)));
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl2), xyToPoint(maxX, lvl2)));
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl3), xyToPoint(maxX, lvl3)));

        Point2D.Double labelPos1 = xyToPoint(0, lvl1);
        Point2D.Double labelPos2 = xyToPoint(0, lvl2);
        Point2D.Double labelPos3 = xyToPoint(0, lvl3);

        canvas.drawString("90%", (float)labelPos1.getX() + 5, (float)(labelPos1.getY() - 3));
        canvas.drawString("50%", (float)labelPos2.getX() + 5, (float)(labelPos2.getY() - 3));
        canvas.drawString("10%", (float)labelPos3.getX() + 5, (float)(labelPos3.getY() - 3));

    }

    private double fix0MAX(final double m) {
        double mm = m;
        int o = 1;
        while (mm < 1.0d) {
            mm = mm * 10;
            o *= 10;
        }
        int i = (int) mm + 1;
        return (double) i / o;
    }

    private double fix1MAX(final double m) {
        double mm = m;
        int o = 1;
        while (mm > 1.0d) {
            mm = mm / 10;
            o *= 10;
        }
        mm *= 10;
        int i = (int) mm + 1;
        o /= 10;
        return (double) i * o;
    }

    protected void paintCaptions(Graphics2D canvas, double step) {

        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        Color oldColor = canvas.getColor();
        Stroke oldStroke = canvas.getStroke();
        Font oldFont = canvas.getFont();
        canvas.setColor(Color.BLACK);
        canvas.setFont(captionFont);

        int xp = (int) xyToPoint(0, 0).x;
        int yp;
        FontRenderContext context = canvas.getFontRenderContext();
        double y = step;
        while (y <= maxY) {
            yp = (int) xyToPoint(0, y).y;
            if (yp < 30)
                break;
            String xs = formatter.format(y);
            Rectangle2D bounds = captionFont.getStringBounds(xs, context);
            canvas.drawString (xs, (int) (xp - 5 - bounds.getWidth()), yp);
            y += step;
        }
        y = -step;

        while (y >= minY) {

            yp = (int) xyToPoint(0, y).y;
            String xs = formatter.format(y);
            Rectangle2D bounds = captionFont.getStringBounds(xs, context);
            canvas.drawString (xs, (int) (xp - 5 - bounds.getWidth()), yp);
            y -= step;
        }

        double x = 0.0d + step;
        yp = (int) xyToPoint(0, 0).y;
        while (x <= maxX) {

            xp = (int) xyToPoint(x, 0).x;
            String xs = formatter.format(x);
            Rectangle2D bounds = captionFont.getStringBounds(xs, context);

            if (xp + (int) (bounds.getWidth() / 2) > getWidth())
                break;


            canvas.drawString (xs, xp - (int) (bounds.getWidth() / 2), yp + 20);
            x += step;
        }
        x = -step;
        while (x >= minX) {
            xp = (int) xyToPoint(x, 0).x;
            String xs = formatter.format(x);
            Rectangle2D bounds3 = captionFont.getStringBounds(xs, context);
            if (xp - (int) (bounds3.getWidth() / 2) < 0)
                break;
            canvas.drawString (xs, xp - (int) (bounds3.getWidth() / 2), yp + 20);
            x -= step;
        }
        canvas.drawString ("0", (int) xyToPoint(0, 0).getX() + 5,
                (int) xyToPoint(0, 0).getY() + 20);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
        canvas.setFont(oldFont);
    }

    // Отрисовка сетки
    protected void paintGrid(Graphics2D canvas) {

        GeneralPath graphics = new GeneralPath();
        double MAX = Math.max(Math.abs(maxX - minX), Math.abs(maxY - minY));
        double step = 0.0f;

        if (MAX / 20 < 1)
            step = fix0MAX(MAX / 20);
        else
            step = fix1MAX(MAX / 20);
        if (PPP) {
            int YY = Math.min(getWidth(), getHeight());
            if (YY < 200)
                step *= 3;
            else if (YY < 400)
                step *= 2;
        }
        Color oldColor = canvas.getColor();
        Stroke oldStroke = canvas.getStroke();
        canvas.setStroke(gridStroke);
        canvas.setColor(Color.GRAY);

        int xp = 0;
        double x = 0.0d;
        int gH = getHeight();
        int gW = getWidth();

        xp = (int) xyToPoint(0, 0).x;
        while (xp > 0) {
            graphics.moveTo(xp, 0);
            graphics.lineTo(xp, gH);
            xp = (int) xyToPoint(x, 0).x;
            x -= step;
        }

        xp = (int) xyToPoint(0, 0).x;
        while (xp < gW) {
            graphics.moveTo(xp, 0);
            graphics.lineTo(xp, gH);
            xp = (int) xyToPoint(x, 0).x;
            x += step;
        }

        int yp = (int) xyToPoint(0, 0).y;
        double y = 0.0f;
        while (yp < gH) {
            yp = (int) xyToPoint(0, y).y;
            graphics.moveTo(0, yp);
            graphics.lineTo(gW, yp);
            y -= step;
        }

        yp = (int) xyToPoint(0, 0).y;
        while (yp > 0) {
            yp = (int) xyToPoint(0, y).y;
            graphics.moveTo(0, yp);
            graphics.lineTo(gW, yp);
            y += step;
        }

        canvas.draw(graphics);
        paintHatch(canvas, 0, 0, step / 10);
        paintCaptions(canvas, step);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    private double averageY(){
        double sum = 0;
        double i = 1;
        for (double[] point: graphicsData)
        {
            sum += point[1];
            i++;
        }
        return sum / i;
    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {

        canvas.setStroke(markerStroke);
        for (double[] point : graphicsData) {

            if (point[1] > 2 * averageY()) {
                canvas.setColor(Color.GREEN);
                canvas.setPaint(Color.GREEN);
            }
            else{
                canvas.setColor(Color.RED);
                canvas.setPaint(Color.RED);
            }


            GeneralPath marker = new GeneralPath();

            Point2D.Double center = xyToPoint(point[0], point[1]);


            Point2D.Double corner1 = shiftPoint(center, -5.5, -5.5);
            Point2D.Double corner2 = shiftPoint(center, 5.5, -5.5);
            Point2D.Double corner3 = shiftPoint(center, 0, 5.5);
            marker.append(new Line2D.Double(corner1, corner2), true);
            marker.append(new Line2D.Double(corner2, corner3), true);
            marker.append(new Line2D.Double(corner3, corner1), true);

            canvas.draw(marker);
            canvas.fill(marker);
        }
    }

    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {

        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Stroke oldStroke = canvas.getStroke();
        Font oldFont = canvas.getFont();

        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();

        if (minX <= 0.0 && maxX >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);

            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()) + 10);
        }

        if (minY <= 0.0 && maxY >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);

            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 20),
                    (float) (labelPos.getY() + bounds.getY()));
        }

        canvas.setColor(oldColor);
        canvas.setPaint(oldPaint);
        canvas.setStroke(oldStroke);
        canvas.setFont(oldFont);
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        if(!zoom)
            return new Point2D.Double(deltaX * scale, deltaY * scale);
        else
            return new Point2D.Double(deltaX * scaleX, deltaY * scaleY);
    }

    protected Point2D.Double pointToXY(int x, int y) {

        Point2D.Double p = new Point2D.Double();

        p.x = x / scale + minX;
        int q = (int) xyToPoint(0, 0).y;
        p.y = maxY - maxY * ((double) y / (double) q);


        return p;
    }


    public class MouseMotionHandler implements MouseMotionListener, MouseListener {

        private double comparePoint(Point p1, Point p2) {
            return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }

        private GraphPoint find(int x, int y) {

            GraphPoint smp = new GraphPoint(), smp2 = new GraphPoint();
            double r, r2 = 1000;

            if (graphicsData != null) {
                for (int i = 0; i < graphicsData.length; i++) {

                    Point point1 = new Point();
                    point1.x = x;
                    point1.y = y;

                    Point point2 = new Point();
                    point2.x = graphicsDataI[i][0];
                    point2.y = graphicsDataI[i][1];
                    r = comparePoint(point1, point2);

                    if (r < 7.0) {
                        smp.x = graphicsDataI[i][0];
                        smp.y = graphicsDataI[i][1];
                        smp.xd = graphicsData[i][0];
                        smp.yd = graphicsData[i][1];

                        smp.n = i;
                        if (r < r2) {
                            r2 = r;
                            smp2 = smp;
                        }
                        return smp2;
                    }
                }
            }
            return null;
        }

        public void mouseMoved(MouseEvent mouseEvent) {

            if(mouseEvent != null){
                GraphPoint smp;
                smp = find(mouseEvent.getX(), mouseEvent.getY());

                if (smp != null) {
                    setCursor(Cursor.getPredefinedCursor(8));
                    SMP = smp;
                }
                else {
                    setCursor(Cursor.getPredefinedCursor(0));
                    SMP = null;
                }
                repaint();
            }
        }

        public void mouseDragged(MouseEvent mouseEvent) {

            if (selMode) {
                rect.setFrame(mausePX, mausePY, mouseEvent.getX() - rect.getX(), mouseEvent.getY() - rect.getY());
                repaint();
            }

            if (dragMode) {
                if(pointToXY(mouseEvent.getX(), mouseEvent.getY()).y < maxY
                        && pointToXY(mouseEvent.getX(), mouseEvent.getY()).y > minY){
                    graphicsData[SMP.n][1] = pointToXY(mouseEvent.getX(), mouseEvent.getY()).y;
                    SMP.yd = pointToXY(mouseEvent.getX(), mouseEvent.getY()).y;
                    SMP.y = mouseEvent.getY();
                    }
                repaint();
            }
        }

        public void mouseClicked(MouseEvent mouseEvent) {

            if (mouseEvent.getButton() != 3)
                return;

            try {
                zone = zoneStack.pop();
            }
            catch (EmptyStackException err) {
            }

            if(zoneStack.empty())
                zoom = false;
            repaint();
        }

        public void mouseEntered(MouseEvent arg0) {

        }

        public void mouseExited(MouseEvent arg0) {

        }

        public void mousePressed(MouseEvent mouseEvent) {

            if (mouseEvent.getButton() != 1)
                return;

            if (SMP != null) {
                selMode = false;
                dragMode = true;
            }
            else {
                dragMode = false;
                selMode = true;
                mausePX = mouseEvent.getX();
                mausePY = mouseEvent.getY();

                rect.setFrame(mouseEvent.getX(), mouseEvent.getY(), 0, 0);
            }
        }

        public void mouseReleased(MouseEvent mouseEvent) {

            rect.setFrame(0, 0, 0, 0);
            if (mouseEvent.getButton() != 1) {
                repaint();
                return;
            }

            if (selMode) {

                if (mouseEvent.getX() <= mausePX || mouseEvent.getY() <= mausePY)
                    return;
                int eventY = mouseEvent.getY();
                int eventX = mouseEvent.getX();

                if (eventY > getHeight())
                    eventY = getHeight();
                if (eventX > getWidth())
                    eventX = getWidth();

                double MAXX = pointToXY(eventX, 0).x;
                double MINX = pointToXY(mausePX, 0).x;
                double MAXY = pointToXY(0, mausePY).y;
                double MINY = pointToXY(0, eventY).y;

                zoneStack.push(zone);
                zone = new Zone();
                zone.use = true;

                zone.MAXX = MAXX;
                zone.MINX = MINX;
                zone.MINY = MINY;
                zone.MAXY = MAXY;
                selMode = false;
                zoom = true;
            }
            repaint();
        }
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {

        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}