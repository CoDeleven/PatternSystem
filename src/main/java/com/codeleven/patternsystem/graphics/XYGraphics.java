package com.codeleven.patternsystem.graphics;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class XYGraphics extends Graphics2D {
    private static final int SCALE = 1;        // 10像素 = 1mm
    private static final int PRESERVE_PX = 100; // 周围预留100个像素
    private final Graphics2D coreGraphics;
    private final BufferedImage coreImage;
    private final int width;                      // 图形宽度，单位毫米
    private final int height;                     // 图形高度，单位毫米
    private final int originX;
    private final int originY;
    public static final Stroke NORMAL_LINE = new BasicStroke(2.0f);
    public static final Stroke DOT_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f, 2.0f}, 0.0f);

    private XYGraphics(int width, int height) {
        this.coreImage = new BufferedImage(width * SCALE, height * SCALE, BufferedImage.TYPE_INT_RGB);
        this.coreGraphics = this.coreImage.createGraphics();
        this.width = width;
        this.height = height;
        originX = width * SCALE / 2;
        originY = height * SCALE / 2;
        this.init();
    }

    private void init(){
        this.coreGraphics.setColor(Color.WHITE);
        this.coreGraphics.setStroke(new BasicStroke(4));
        this.coreGraphics.fillRect(0, 0, width * SCALE, height * SCALE);
        this.coreGraphics.setColor(Color.BLACK);
    }

    public static XYGraphics createGraphics(int width, int height){
        return new XYGraphics(width, height);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        x1 = computeX(x1);
        x2 = computeX(x2);
        y1 = computeY(y1);
        y2 = computeY(y2);
        this.coreGraphics.drawLine(x1, y1, x2, y2);
    }

    public void drawArrow(int x1, int y1, int x2, int y2, int len){
        Path2D path = new Path2D.Double();
        double t = len;
        double w = len * .5;
        path.moveTo(0d, -w);
        path.lineTo(t, 0d);
        path.lineTo(0d, w);
        AffineTransform at = AffineTransform.getTranslateInstance(computeX(x1), computeY(y1));
        at.rotate(Math.atan(((double)computeY(y2) - computeY(y1)) / (computeX(x2) - computeX(x1))));
        path.transform(at);
        this.coreGraphics.draw(path);
    }

    public void drawRect(int x, int y, int width, int height){
        x = computeX(x);
        y = computeY(y);
        this.coreGraphics.drawRect(x - (width / 2), y - (height / 2), width, height);
    }

    public void outputTo(String filePath) throws IOException {
        ImageIO.write(this.coreImage, "PNG", new FileOutputStream(filePath));
    }

    public OutputStream outputStream(){
        OutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(this.coreImage, "PNG", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }

    private int computeX(int x){
        return x += originX;
    }

    private int computeY(int y){
        y = y > 0 ? -y : Math.abs(y);
        return y += originY;
    }

    /**
     * 计算一条直线 与 坐标轴呈现的弧度（与X正方向）
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double computeAngleWithCoordinate(int x1, int y1, int x2, int y2) {
        if(x1 == x2){
            return Double.MAX_VALUE;
        }
        float k = (y1 - y2) / (x1 - x2);
        return Math.atan(k);
    }

    @Override
    public void draw(Shape s) {

    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return false;
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {

    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {

    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {

    }

    @Override
    public void drawString(String str, int x, int y) {

    }

    @Override
    public void drawString(String str, float x, float y) {

    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {

    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {

    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {

    }

    @Override
    public void fill(Shape s) {

    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return false;
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return null;
    }

    @Override
    public void setComposite(Composite comp) {

    }

    @Override
    public void setPaint(Paint paint) {

    }

    @Override
    public void setStroke(Stroke s) {
        this.coreGraphics.setStroke(s);
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {

    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return null;
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {

    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {

    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public void translate(double tx, double ty) {

    }

    @Override
    public void rotate(double theta) {

    }

    @Override
    public void rotate(double theta, double x, double y) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double shx, double shy) {

    }

    @Override
    public void transform(AffineTransform Tx) {

    }

    @Override
    public void setTransform(AffineTransform Tx) {

    }

    @Override
    public AffineTransform getTransform() {
        return null;
    }

    @Override
    public Paint getPaint() {
        return null;
    }

    @Override
    public Composite getComposite() {
        return null;
    }

    @Override
    public void setBackground(Color color) {

    }

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public Stroke getStroke() {
        return null;
    }

    @Override
    public void clip(Shape s) {

    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return null;
    }

    @Override
    public Graphics create() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color c) {
        this.coreGraphics.setColor(c);
    }

    @Override
    public void setPaintMode() {

    }

    @Override
    public void setXORMode(Color c1) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public Rectangle getClipBounds() {
        return null;
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {

    }

    @Override
    public void setClip(int x, int y, int width, int height) {

    }

    @Override
    public Shape getClip() {
        return null;
    }

    @Override
    public void setClip(Shape clip) {

    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {

    }

    @Override
    public void fillRect(int x, int y, int width, int height) {

    }

    @Override
    public void clearRect(int x, int y, int width, int height) {

    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void drawOval(int x, int y, int width, int height) {

    }

    @Override
    public void fillOval(int x, int y, int width, int height) {

    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public void dispose() {

    }
}
